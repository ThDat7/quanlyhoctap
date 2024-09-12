package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.MidtermExamScoreUpdateRequest;
import com.thanhdat.quanlyhoctap.dto.response.TeacherScoreResponse;
import com.thanhdat.quanlyhoctap.entity.FactorScore;
import com.thanhdat.quanlyhoctap.entity.Score;
import com.thanhdat.quanlyhoctap.entity.Study;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.repository.StudyRepository;
import com.thanhdat.quanlyhoctap.service.ScoreService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import static com.thanhdat.quanlyhoctap.specification.StudySpecification.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private StudyRepository studyRepository;
    private TeacherService teacherService;
    private CourseClassRepository courseClassRepository;
    public List<TeacherScoreResponse> getByCourseClassAndCurrentTeacher(Integer courseClassId) {
        validateTeacherCanGetScoreResponse(courseClassId);

        List<Study> studies = studyRepository.findByCourseClassId(courseClassId);
        return mapToTeacherScoreResponse(studies);
    }

    @Override
    @Transactional
    public void updateByCurrentTeacher(List<MidtermExamScoreUpdateRequest> midtermExamScoreUpdateRequests) {
        List<Integer> requestIds = midtermExamScoreUpdateRequests.stream()
                .map(MidtermExamScoreUpdateRequest::getStudyId)
                .collect(Collectors.toList());

        validateTeacherCanUpdateScoreRequest(requestIds);

        List<Study> studies = studyRepository.findAllById(requestIds);

        studies.forEach(study -> {
            MidtermExamScoreUpdateRequest matchRequest = midtermExamScoreUpdateRequests.stream()
                    .filter(request -> request.getStudyId().equals(study.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Study not found"));

            Optional<Score> opMidtermScore = study.getScores().stream()
                    .filter(score -> score.getFactorScore().equals(FactorScore.PROCESS))
                    .findFirst();

            Score midtermScore = opMidtermScore.get();

            if (midtermScore == null) {
                midtermScore = Score.builder()
                        .factorScore(FactorScore.PROCESS)
                        .study(study)
                        .build();
                study.getScores().add(midtermScore);
            }

            midtermScore.setScore(matchRequest.getScore());
        });

        studyRepository.saveAll(studies);
    }

    private void validateTeacherCanUpdateScoreRequest(List<Integer> requestIds) {
        int currentTeacherId = teacherService.getCurrentTeacherId();

        Specification<Study> rootSpecification = Specification.where(null);
        rootSpecification = rootSpecification.and(belongsToTeacherId(currentTeacherId));
        rootSpecification = rootSpecification.and(inListIds(requestIds));
        rootSpecification = rootSpecification.and(haveCourseClassInNoneLockedSemester());

        long countValidRequest = studyRepository.count(rootSpecification);

        Boolean isRequestValid = countValidRequest == requestIds.size();
        if (!isRequestValid)
            throw new RuntimeException("Teacher is not teach this course class");
    }

    private void validateTeacherCanGetScoreResponse(Integer courseClassId) {

        int currentTeacherId = teacherService.getCurrentTeacherId();
        Boolean isTeacherTeachCourseClass = courseClassRepository.existsByIdAndTeacherId(courseClassId, currentTeacherId);

        if (!isTeacherTeachCourseClass)
            throw new RuntimeException("Teacher is not teach this course class");

        Boolean isCourseClassInUnLockedSemester = courseClassRepository.existsByIdAndSemesterNotLocked(courseClassId);
        if (!isCourseClassInUnLockedSemester)
            throw new RuntimeException("Course class is not in un locked semester");
    }

    private List<TeacherScoreResponse> mapToTeacherScoreResponse(List<Study> studies) {
        return studies.stream()
                .map(study -> {
                    Float processScore = (float) study.getScores().stream()
                            .filter(score -> score.getFactorScore().equals(FactorScore.PROCESS))
                            .mapToDouble(score -> score.getScore())
                            .sum();
                    return TeacherScoreResponse.builder()
                            .studyId(study.getId())
                            .studentFirstName(study.getStudent().getUser().getFirstName())
                            .studentLastName(study.getStudent().getUser().getLastName())
                            .score(processScore)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
