package com.thanhdat.quanlyhoctap.specification;

import com.thanhdat.quanlyhoctap.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import static com.thanhdat.quanlyhoctap.specification.JoinHelper.join;

public class CourseOutlineSpecification {
    public static Specification<CourseOutline> courseNameLike(String kw) {
        return (root, query, cb) -> {
            Join<CourseOutline, Course> courseJoin = join(root, CourseOutline_.course, JoinType.INNER);
            return cb.like(courseJoin.get(Course_.name), "%" + kw + "%");
        };
    }

    public static Specification<CourseOutline> statusEqual(OutlineStatus status) {
        return (root, query, cb) -> cb.equal(root.get(CourseOutline_.status), status);
    }

    public static Specification<CourseOutline> courseEqual(Integer courseId) {
        return (root, query, cb) -> {
            Join<CourseOutline, Course> courseJoin = join(root, CourseOutline_.course, JoinType.INNER);
            return cb.equal(courseJoin.get(Course_.id), courseId);
        };
    }

    public static Specification<CourseOutline> belongsToMajor(Integer majorId) {
        return (root, query, cb) -> {
            Join<CourseOutline, EducationProgramCourse> educationProgramCourseJoin = join(root, CourseOutline_.educationProgramCourses, JoinType.INNER);
            Join<EducationProgramCourse, EducationProgram> educationProgramJoin = join(educationProgramCourseJoin, EducationProgramCourse_.educationProgram, JoinType.INNER);
            Join<EducationProgram, Major> majorJoin = join(educationProgramJoin, EducationProgram_.major, JoinType.INNER);
            return cb.equal(majorJoin.get(Major_.id), majorId);
        };
    }

    public static Specification<CourseOutline> belongsToTeacherId(Integer teacherId) {
        return (root, query, cb) -> {
            Join<CourseOutline, Teacher> teacherJoin = join(root, CourseOutline_.teacher, JoinType.INNER);
            return cb.equal(teacherJoin.get(Teacher_.id), teacherId);
        };
    }

    public static Specification<CourseOutline> idEqual(Integer id) {
        return (root, query, cb) -> cb.equal(root.get(CourseOutline_.id), id);
    }

    public static Specification<CourseOutline> yearPublishedEqual(Integer year) {
        return (root, query, cb) -> cb.equal(root.get(CourseOutline_.yearPublished), year);
    }

    public static Specification<CourseOutline> belongsToEducationProgramBySchoolYear(Integer year) {
        return (root, query, cb) -> {
            Join<CourseOutline, EducationProgramCourse> educationProgramCourseJoin = join(root, CourseOutline_.educationProgramCourses, JoinType.INNER);
            Join<EducationProgramCourse, EducationProgram> educationProgramJoin = join(educationProgramCourseJoin, EducationProgramCourse_.educationProgram, JoinType.INNER);
            return cb.equal(educationProgramJoin.get(EducationProgram_.schoolYear), year);
        };
    }

    public static Specification<CourseOutline> teacherNameLike(String kw) {
        return (root, query, cb) -> {
            Join<CourseOutline, Teacher> teacherJoin = join(root, CourseOutline_.teacher, JoinType.INNER);
            Join<Teacher, User> userJoin = join(teacherJoin, Teacher_.user, JoinType.INNER);
            return cb.like(cb.concat(cb.concat(userJoin.get(User_.lastName), " "), userJoin.get(User_.firstName)), "%" + kw + "%");
        };
    }

    public static Specification<CourseOutline> haveUrl() {
        return (root, query, cb) -> cb.and(cb.isNotNull(root.get(CourseOutline_.url)),
                cb.notEqual(root.get(CourseOutline_.url), ""));
    }

    public static Specification<CourseOutline> isPublished() {
        return (root, query, cb) -> cb.equal(root.get(CourseOutline_.status), OutlineStatus.PUBLISHED);
    }

    public static Specification<CourseOutline> courseCreditsEqual(float credits) {
        return (root, query, cb) -> {
            Join<CourseOutline, Course> courseJoin = join(root, CourseOutline_.course, JoinType.INNER);
            return cb.equal(courseJoin.get(Course_.credits), credits);
        };
    }
}