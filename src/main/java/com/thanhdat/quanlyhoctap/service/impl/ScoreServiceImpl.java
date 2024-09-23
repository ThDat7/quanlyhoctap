package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.ScoreUpdateRequest;
import com.thanhdat.quanlyhoctap.dto.response.ScoreResponse;
import com.thanhdat.quanlyhoctap.entity.FactorScore;
import com.thanhdat.quanlyhoctap.entity.Score;
import com.thanhdat.quanlyhoctap.entity.Study;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.mapper.ScoreMapper;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.repository.StudyRepository;
import com.thanhdat.quanlyhoctap.service.ScoreService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import static com.thanhdat.quanlyhoctap.specification.StudySpecification.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreServiceImpl implements ScoreService {
    StudyRepository studyRepository;
    CourseClassRepository courseClassRepository;

    TeacherService teacherService;

    ScoreMapper scoreMapper;

    public List<ScoreResponse> getByCourseClassAndCurrentTeacher(Long courseClassId) {
        validateTeacherCanGetScoreResponse(courseClassId);

        List<Study> studies = studyRepository.findByCourseClassId(courseClassId);
        List<Score> midtermScores = studies.stream()
                .map(Study::getScores)
                .flatMap(e -> e.stream())
                .filter(score -> score.getFactorScore().equals(FactorScore.PROCESS))
                .collect(Collectors.toList());
        return midtermScores.stream()
                .map(scoreMapper::toScoreResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateByCurrentTeacher(List<ScoreUpdateRequest> scoreUpdateRequests) {
        List<Long> requestIds = scoreUpdateRequests.stream()
                .map(ScoreUpdateRequest::getStudyId)
                .collect(Collectors.toList());

        validateTeacherCanUpdateScoreRequest(requestIds);
        updateScore(scoreUpdateRequests, FactorScore.PROCESS);
    }

    private void updateScore(List<ScoreUpdateRequest> scoreUpdateRequests, FactorScore factorScore) {
        List<Long> requestIds = scoreUpdateRequests.stream()
                .map(ScoreUpdateRequest::getStudyId)
                .collect(Collectors.toList());

        List<Study> studies = studyRepository.findAllById(requestIds);

        studies.forEach(study -> {
            ScoreUpdateRequest matchRequest = scoreUpdateRequests.stream()
                    .filter(request -> request.getStudyId().equals(study.getId()))
                    .findFirst()
//                    or study not found?
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_TEACH_COURSE_CLASS));

            Score score = study.getScores().stream()
                    .filter(e -> e.getFactorScore().equals(factorScore))
                    .findFirst()
                    .orElseGet(() -> {
                        Score newscore = Score.builder()
                                .factorScore(factorScore)
                                .study(study)
                                .build();
                        study.getScores().add(newscore);
                        return newscore;
                    });

            score.setScore(matchRequest.getScore());
        });

        studyRepository.saveAll(studies);
    }

//    refactor: logic of validate update and get score is same. validateTeacherCanActionOnScore
    private void validateTeacherCanUpdateScoreRequest(List<Long> requestIds) {
        Long currentTeacherId = teacherService.getCurrentTeacherId();

        Specification<Study> rootSpecification = Specification.where(null);
        rootSpecification = rootSpecification.and(belongsToTeacherId(currentTeacherId));
        rootSpecification = rootSpecification.and(inListIds(requestIds));
        rootSpecification = rootSpecification.and(haveCourseClassInNoneLockedSemester());

        long countValidRequest = studyRepository.count(rootSpecification);

        Boolean isRequestValid = countValidRequest == requestIds.size();
        if (!isRequestValid)
            throw new AppException(ErrorCode.NOT_TEACH_COURSE_CLASS);
    }

    private void validateTeacherCanGetScoreResponse(Long courseClassId) {

        Long currentTeacherId = teacherService.getCurrentTeacherId();
        Boolean isTeacherTeachCourseClass = courseClassRepository.existsByIdAndTeacherId(courseClassId, currentTeacherId);

        if (!isTeacherTeachCourseClass)
            throw new AppException(ErrorCode.NOT_TEACH_COURSE_CLASS);

        Boolean isCourseClassInUnLockedSemester = courseClassRepository.existsByIdAndSemesterNotLocked(courseClassId);
        if (!isCourseClassInUnLockedSemester)
            throw new AppException(ErrorCode.COURSE_CLASS_LOCKED);
    }
}
