package com.thanhdat.quanlyhoctap.specification;

import com.thanhdat.quanlyhoctap.entity.*;
import org.springframework.data.jpa.domain.Specification;

public class CourseOutlineSpecification {
    public static Specification<CourseOutline> courseNameLike(String kw) {
        return (root, query, cb) -> cb.like(root.get(CourseOutline_.course).get(Course_.name), "%" + kw + "%");
    }

    public static Specification<CourseOutline> statusEqual(OutlineStatus status) {
        return (root, query, cb) -> cb.equal(root.get(CourseOutline_.status), status);
    }

    public static Specification<CourseOutline> courseEqual(Integer courseId) {
        return (root, query, cb) -> cb.equal(root.get(CourseOutline_.course).get(Course_.id), courseId);
    }

    public static Specification<CourseOutline> belongsToMajor(Integer majorId) {
        return (root, query, cb) -> cb.equal(root
                .join(CourseOutline_.educationProgramCourses)
                .join(EducationProgramCourse_.educationProgram)
                .join(EducationProgram_.major).get(Major_.id), majorId);
    }

    public static Specification<CourseOutline> yearPublishedEqual(Integer year) {
        return (root, query, cb) -> cb.equal(root.get(CourseOutline_.yearPublished), year);
    }

    public static Specification<CourseOutline> belongsToEducationProgramBySchoolYear(Integer year) {
        return (root, query, cb) -> cb.equal(root
                .join(CourseOutline_.educationProgramCourses)
                .join(EducationProgramCourse_.educationProgram).get(EducationProgram_.schoolYear), year);
    }

    public static Specification<CourseOutline> teacherNameLike(String kw) {
        return (root, query, cb) -> cb.like(cb.concat(
                cb.concat(root.get(CourseOutline_.teacher).get(Teacher_.user).get(User_.lastName), " "),
                        root.get(CourseOutline_.teacher).get(Teacher_.user).get(User_.firstName)), "%" + kw + "%");
    }

    public static Specification<CourseOutline> haveUrl() {
        return (root, query, cb) -> cb.and(cb.isNotNull(root.get(CourseOutline_.url)),
                cb.notEqual(root.get(CourseOutline_.url), ""));
    }

    public static Specification<CourseOutline> isPublished() {
        return (root, query, cb) -> cb.equal(root.get(CourseOutline_.status), OutlineStatus.PUBLISHED);
    }

    public static Specification<CourseOutline> courseCreditsEqual(float credits) {
        return (root, query, cb) -> cb.equal(root.get(CourseOutline_.course).get(Course_.credits), credits);
    }
}