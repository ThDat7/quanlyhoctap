package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.StudyResultCourseResponse;
import com.thanhdat.quanlyhoctap.dto.response.StudyResultSemesterResponse;
import com.thanhdat.quanlyhoctap.entity.CourseClass;
import com.thanhdat.quanlyhoctap.entity.FactorScore;
import com.thanhdat.quanlyhoctap.entity.Semester;
import com.thanhdat.quanlyhoctap.entity.Study;
import com.thanhdat.quanlyhoctap.repository.StudyRepository;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.StudyResultService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudyResultServiceImpl implements StudyResultService {
    private StudentService studentService;
    private StudyRepository studyRepository;

    @Override
    public List<StudyResultSemesterResponse> getByCurrentStudent() {
        int currentStudentId = studentService.getCurrentStudentId();
        List<Study> studies = new ArrayList<>(studyRepository.findByStudentId(currentStudentId));
        return convertToResponses(studies);
    }


    private List<StudyResultSemesterResponse> convertToResponses(List<Study> studies) {
//        if re-study replace old score to earn

        Map<Semester, List<Study>> studiesBySemester = studies.stream()
                .collect(Collectors.groupingBy(study -> study.getCourseClass().getSemester()));

        List<StudyResultSemesterResponse> responses =
                studiesBySemester.entrySet().stream()
                .map(entry -> convertToResponse(entry.getKey(), entry.getValue()))
                .sorted((o1, o2) -> {
                    if (o1.getYear().equals(o2.getYear())) {
                        return o1.getSemester().compareTo(o2.getSemester());
                    }
                    return o1.getYear().compareTo(o2.getYear());
                })
                .collect(Collectors.toList());
        for (int i = 0; i < responses.size(); i++) {
            Float GPA4EarnedThisSemester = responses.get(i).getGPA4Semester();
            Float creditsEarnedThisSemester = responses.get(i).getCreditsEarnedSemester();

            if (i == 0) {
                responses.get(i).setGPA4Cumulative(responses.get(i).getGPA4Semester());
                responses.get(i).setCreditsCumulative(creditsEarnedThisSemester);
            } else {
                Float previousGPA4Cumulative = responses.get(i - 1).getGPA4Cumulative();
                Float previousCreditsCumulative = responses.get(i - 1).getCreditsCumulative();

                responses.get(i).setCreditsCumulative(previousCreditsCumulative + creditsEarnedThisSemester);

                Float currentGPA4Cumulative = (previousGPA4Cumulative * previousCreditsCumulative +
                        GPA4EarnedThisSemester * creditsEarnedThisSemester) /
                        responses.get(i).getCreditsCumulative();
                currentGPA4Cumulative = Math.round(currentGPA4Cumulative * 100) / 100.0f;
                responses.get(i).setGPA4Cumulative(currentGPA4Cumulative);
            }
        }
        Collections.reverse(responses);
        return responses;
    }

    private StudyResultSemesterResponse convertToResponse(Semester semester, List<Study> studies) {
        List<StudyResultCourseResponse> courses = studies.stream()
                .map(this::courseResponses)
                .collect(Collectors.toList());

        //        Will re-system to earn credits only education program requirements
        Float creditsEarnedSemester = (float) courses.stream()
                .mapToDouble(c -> {
                    if (c.getIsPassed())
                        return c.getCredits();
                    return 0d;
                })
                .sum();
        creditsEarnedSemester = Math.round(creditsEarnedSemester * 10) / 10.0f;

        Float GPA4Semester = (float) courses.stream()
                .mapToDouble(c -> c.getTotalScore4() * c.getCredits())
                .sum() / creditsEarnedSemester;
        GPA4Semester = Math.round(GPA4Semester * 100) / 100.0f;

        return StudyResultSemesterResponse.builder()
                .semester(semester.getSemester())
                .year(semester.getYear())
                .courses(courses)
                .creditsEarnedSemester(creditsEarnedSemester)
                .GPA4Semester(GPA4Semester)
                .build();
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

    private StudyResultCourseResponse courseResponses(Study study) {
        CourseClass courseClass = study.getCourseClass();
        Float midTermFactor = courseClass.getCourseRule().getMidTermFactor();
        Float finalTermFactor = courseClass.getCourseRule().getFinalTermFactor();
        Float passScore = courseClass.getCourseRule().getPassScore();

        Float midTermScore = study.getScores().stream()
                .filter(score -> score.getFactorScore().equals(FactorScore.PROCESS)).findFirst().get().getScore();
        Float finalTermScore = study.getScores().stream()
                .filter(score -> score.getFactorScore().equals(FactorScore.FINAL)).findFirst().get().getScore();
        Float totalScore10 = Math.round((midTermScore * midTermFactor + finalTermScore * finalTermFactor) * 10) / 10.0f;
        Float totalScore4 = convertToTotalScore4(totalScore10);
        String totalScoreLetter = convertToTotalScoreLetter(totalScore10);
        Boolean isPassed = totalScore10 >= passScore;

        return StudyResultCourseResponse.builder()
                .courseCode(courseClass.getCourse().getCode())
                .courseName(courseClass.getCourse().getName())
                .credits(courseClass.getCourse().getCredits())
                .studentClassName(study.getStudent().getStudentClass().getName())
                .midTermScore(midTermScore)
                .finalTermScore(finalTermScore)
                .totalScore10(totalScore10)
                .totalScore4(totalScore4)
                .totalScoreLetter(totalScoreLetter)
                .isPassed(isPassed)
                .build();
    }
}