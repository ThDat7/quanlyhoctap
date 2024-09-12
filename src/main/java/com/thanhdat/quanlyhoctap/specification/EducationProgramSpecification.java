package com.thanhdat.quanlyhoctap.specification;

import com.thanhdat.quanlyhoctap.entity.EducationProgram;
import com.thanhdat.quanlyhoctap.entity.EducationProgram_;
import com.thanhdat.quanlyhoctap.entity.Major;
import com.thanhdat.quanlyhoctap.entity.Major_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import static com.thanhdat.quanlyhoctap.specification.JoinHelper.join;

public class EducationProgramSpecification {

    public static Specification<EducationProgram> nameLike(String kw) {

        return (root, query, cb) -> {
            Join<EducationProgram, Major> majorJoin = join(root, EducationProgram_.major, JoinType.INNER);
            return cb.like(majorJoin.get(Major_.name), "%" + kw + "%");
        };
    }

    public static Specification<EducationProgram> schoolYearEqual(int year) {
        return (root, query, cb) -> cb.equal(root.get(EducationProgram_.schoolYear), year);
    }

    public static Specification<EducationProgram> majorEqual(int majorId) {
        return (root, query, cb) -> {
            Join<EducationProgram, Major> majorJoin = join(root, EducationProgram_.major, JoinType.INNER);
            return cb.equal(majorJoin.get(Major_.id), majorId);
        };
    }
}