package com.thanhdat.quanlyhoctap.specification;

import com.thanhdat.quanlyhoctap.entity.*;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.thanhdat.quanlyhoctap.specification.JoinHelper.join;

public class CourseClassSpecification {

    public static Specification<CourseClass> semesterIdEqual(Long semesterId) {
        return (root, query, cb) -> {
            Join<CourseClass, Semester> semesterJoin = join(root, CourseClass_.semester, JoinType.INNER);
            return cb.equal(semesterJoin.get(Semester_.id), semesterId);
        };
    }

    public static Specification<CourseClass> courseIdEqual(Long courseId) {
        return (root, query, cb) -> {
            Join<CourseClass, Course> courseJoin = join(root, CourseClass_.course, JoinType.INNER);
            return cb.equal(courseJoin.get(Course_.id), courseId);
        };
    }

    public static Specification<CourseClass> haveAnyStudentStudyIn(List<Long> studentIds) {
        return (root, query, cb) -> {
            Join<CourseClass, Study> studyJoin = join(root, CourseClass_.studies, JoinType.INNER);
            return studyJoin.get(Study_.student).get(Student_.id).in(studentIds);
        };
    }
}
