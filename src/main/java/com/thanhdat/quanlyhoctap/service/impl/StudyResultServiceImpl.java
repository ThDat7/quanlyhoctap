package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.StudyResultCourseResponse;
import com.thanhdat.quanlyhoctap.dto.response.StudyResultSemesterResponse;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.mapper.StudyMapper;
import com.thanhdat.quanlyhoctap.repository.StudyRepository;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.StudyResultService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudyResultServiceImpl implements StudyResultService {
    StudyRepository studyRepository;

    StudentService studentService;

    StudyMapper studyMapper;

    @Override
    public List<StudyResultSemesterResponse> getByCurrentStudent() {
        Long currentStudentId = studentService.getCurrentStudentId();
        List<Study> studies = new ArrayList<>(studyRepository.findByStudentId(currentStudentId));
        return calculate(studies);
    }

    private List<StudyResultSemesterResponse> calculate(List<Study> studies) {
//        if re-study replace old score to earn

        Map<Semester, List<Study>> studiesBySemester = studies.stream()
                .collect(Collectors.groupingBy(study -> study.getCourseClass().getSemester()));

        List<StudyResultSemesterResponse> oldToNewStudyResultSemesters = studiesBySemester.entrySet().stream()
                        .map(entry -> calculateGPAAndCreditsSemesters(entry.getKey(), entry.getValue()))
                        .sorted(this::compareSemester)
                        .collect(Collectors.toList());

        calculateGPAAndCreditsCumulative(oldToNewStudyResultSemesters);
        roundResponses(oldToNewStudyResultSemesters);

        List<StudyResultSemesterResponse> newToOldStudyResultSemesters =
                oldToNewStudyResultSemesters.subList(0, oldToNewStudyResultSemesters.size());
        Collections.reverse(newToOldStudyResultSemesters);

        return newToOldStudyResultSemesters;
    }

    private void roundResponses(List<StudyResultSemesterResponse> responses) {
        for (StudyResultSemesterResponse response : responses) {
            response.setGPA4Semester(Math.round(response.getGPA4Semester() * 100) / 100f);
            response.setGPA4Cumulative(Math.round(response.getGPA4Cumulative() * 100) / 100f);

            for (StudyResultCourseResponse course : response.getCourses())
                if (course.getTotalScore10() != null)
                    course.setTotalScore10(Math.round(course.getTotalScore10() * 10) / 10f);
        }
    }

    private int compareSemester(StudyResultSemesterResponse o1, StudyResultSemesterResponse o2) {
        if (o1.getYear().equals(o2.getYear())) {
            return o1.getSemester().compareTo(o2.getSemester());
        }
        return o1.getYear().compareTo(o2.getYear());
    }

    private StudyResultSemesterResponse calculateGPAAndCreditsSemesters(Semester semester, List<Study> studies) {
        List<StudyResultCourseResponse> resultCourses = studies.stream()
                .map(this::calculateResultCourse)
                .collect(Collectors.toList());

        //        Will re-system to earn credits only education program requirements
        Float creditsEarnedSemester = (float) resultCourses.stream()
                .mapToDouble(c -> c.getIsPassed() ? c.getCredits() : 0)
                .sum();

        Float allCredits = (float) resultCourses.stream()
                .mapToDouble(e -> e.getTotalScore4() != null ? e.getCredits() : 0)
                .sum();
        Float GPA4Semester = (float) resultCourses.stream()
                .mapToDouble(c -> c.getTotalScore4() != null ? c.getTotalScore4() * c.getCredits() : 0)
                .sum() / allCredits;
        if (allCredits == 0) GPA4Semester = 0f;

        return studyMapper.toStudyResultSemesterResponse(semester, creditsEarnedSemester, GPA4Semester, resultCourses);
    }

    private void calculateGPAAndCreditsCumulative(List<StudyResultSemesterResponse> studyResultSemesters) {
        Map<String, StudyResultCourseResponse> courseResultWithCourseCode = new HashMap<>();
        for (StudyResultSemesterResponse studyResultSemester : studyResultSemesters) {
            studyResultSemester.getCourses().stream()
                    .forEach(course -> {
                        Boolean isHaveScore4 = course.getTotalScore4() != null;
                        if (!isHaveScore4) return;

                        courseResultWithCourseCode.merge(course.getCourseCode(), course, (oldCourse, newCourse) -> {
                            Boolean isOldCourseScoreLower = oldCourse.getTotalScore4() < course.getTotalScore4();
                            return isOldCourseScoreLower ? newCourse : oldCourse;
                        });
                    });

            float sumCreditsCumulative = (float) courseResultWithCourseCode.values().stream()
                    .filter(StudyResultCourseResponse::getIsPassed)
                    .mapToDouble(StudyResultCourseResponse::getCredits)
                    .sum();

            float sumScore4 = (float) courseResultWithCourseCode.values().stream()
                    .filter(course -> course.getTotalScore4() != null)
                    .mapToDouble(course -> course.getTotalScore4() * course.getCredits())
                    .sum();

            float sumCredits = (float) courseResultWithCourseCode.values().stream()
                    .filter(course -> course.getTotalScore4() != null)
                    .mapToDouble(StudyResultCourseResponse::getCredits)
                    .sum();

            Float GPA4Cumulative = sumScore4 / sumCredits;
            studyResultSemester.setCreditsCumulative(sumCreditsCumulative);
            studyResultSemester.setGPA4Cumulative(GPA4Cumulative);
        }
    }

    private Float convertToTotalScore4(Float totalScore10) {
        if (totalScore10 == null) {
            return null;
        }

        if (totalScore10 >= 8.5) {
            return 4.0f;
        } else if (totalScore10 >= 8.0) {
            return 3.5f;
        } else if (totalScore10 >= 7.0) {
            return 3.0f;
        } else if (totalScore10 >= 6.5) {
            return 2.5f;
        } else if (totalScore10 >= 5.5) {
            return 2.0f;
        } else if (totalScore10 >= 5.0) {
            return 1.5f;
        } else if (totalScore10 >= 4.0) {
            return 1.0f;
        } else {
            return 0.0f;
        }


    }

    private String convertToTotalScoreLetter(Float totalScore10) {
        if (totalScore10 == null) {
            return null;
        }

        if (totalScore10 >= 9.0) {
            return "A+";
        } else if (totalScore10 >= 8.5) {
            return "A";
        } else if (totalScore10 >= 8.0) {
            return "B+";
        } else if (totalScore10 >= 7.0) {
            return "B";
        } else if (totalScore10 >= 6.5) {
            return "C+";
        } else if (totalScore10 >= 5.5) {
            return "C";
        } else if (totalScore10 >= 5.0) {
            return "D+";
        } else if (totalScore10 >= 4.0) {
            return "D";
        } else {
            return "F";
        }
    }

    private Float calculateScore10(Float midTermFactor, Float finalTermFactor, Float midTermScore, Float finalTermScore) {
        if (midTermFactor == null || finalTermFactor == null || midTermScore == null || finalTermScore == null) {
            return null;
        }

        return midTermScore * midTermFactor + finalTermScore * finalTermFactor;
    }

    private StudyResultCourseResponse calculateResultCourse(Study study) {
        CourseClass courseClass = study.getCourseClass();
        Float midTermFactor = courseClass.getCourseRule().getMidTermFactor();
        Float finalTermFactor = courseClass.getCourseRule().getFinalTermFactor();
        Float passScore = courseClass.getCourseRule().getPassScore();

        Optional<Score> midTermOp = study.getScores().stream()
                .filter(score -> score.getFactorScore().equals(FactorScore.PROCESS)).findFirst();
        Float midTermScore = midTermOp.map(Score::getScore).orElse(null);

        Optional<Score> finalTermOp = study.getScores().stream()
                .filter(score -> score.getFactorScore().equals(FactorScore.FINAL)).findFirst();
        Float finalTermScore = finalTermOp.map(Score::getScore).orElse(null);

        Float totalScore10 = calculateScore10(midTermFactor, finalTermFactor, midTermScore, finalTermScore);
        Float totalScore4 = convertToTotalScore4(totalScore10);
        String totalScoreLetter = convertToTotalScoreLetter(totalScore10);
        Boolean isPassed = totalScore10 != null && passScore != null && totalScore10 >= passScore;

        return studyMapper.toStudyResultCourseResponse(courseClass, midTermScore, finalTermScore, totalScore10,
                totalScore4, totalScoreLetter, isPassed);
    }
}