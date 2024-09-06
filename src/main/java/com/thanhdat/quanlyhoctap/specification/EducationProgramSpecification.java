package com.thanhdat.quanlyhoctap.specification;

import com.thanhdat.quanlyhoctap.entity.EducationProgram;
import com.thanhdat.quanlyhoctap.entity.EducationProgram_;
import com.thanhdat.quanlyhoctap.entity.Major_;
import org.springframework.data.jpa.domain.Specification;

public class EducationProgramSpecification {

    public static Specification<EducationProgram> nameLike(String kw) {
        return (root, query, cb) -> cb.like(root.get(EducationProgram_.major).get(Major_.name), "%" + kw + "%");
    }

    public static Specification<EducationProgram> schoolYearEqual(int year) {
        return (root, query, cb) -> cb.equal(root.get(EducationProgram_.schoolYear), year);
    }

    public static Specification<EducationProgram> majorEqual(int majorId) {
        return (root, query, cb) -> cb.equal(root.get(EducationProgram_.major).get(Major_.id), majorId);
    }
}