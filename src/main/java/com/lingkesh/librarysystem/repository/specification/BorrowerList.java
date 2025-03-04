package com.lingkesh.librarysystem.repository.specification;

import com.lingkesh.librarysystem.entity.Borrower;
import org.springframework.data.jpa.domain.Specification;

public class BorrowerList {

    public static Specification<Borrower> hasId(String id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    public static Specification<Borrower> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("name"), name);
        };
    }

    public static Specification<Borrower> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }
}
