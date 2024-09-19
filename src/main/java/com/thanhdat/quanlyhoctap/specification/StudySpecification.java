package com.thanhdat.quanlyhoctap.specification;

import com.thanhdat.quanlyhoctap.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import static com.thanhdat.quanlyhoctap.specification.JoinHelper.join;

public class StudySpecification {

    public static Specification<Study> belongsToTeacherId(Long teacherId) {
        return (root, query, cb) -> {
            Join<Study, CourseClass> courseClassJoin = join(root, Study_.courseClass, JoinType.INNER);
            Join<CourseClass, Teacher> teacherJoin = join(courseClassJoin, CourseClass_.teacher, JoinType.INNER);
            return cb.equal(teacherJoin.get(Teacher_.id), teacherId);
        };
    }

    public static Specification<Study> haveCourseClassInNoneLockedSemester() {
        LocalDateTime now = LocalDateTime.now();

        return (root, query, cb) -> {
            Join<Study, CourseClass> courseClassJoin = join(root, Study_.courseClass, JoinType.INNER);
                Join<CourseClass, Semester> semesterJoin = join(courseClassJoin, CourseClass_.semester, JoinType.INNER);
                return cb.greaterThan(semesterJoin.get(Semester_.lockTime), now);
        };
    }

    public static Specification<Study> inListIds(List<Long> ids) {
        return (root, query, cb) -> root.get(Study_.id).in(ids);
    }
}
