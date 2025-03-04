package com.lingkesh.librarysystem.repository;

import com.lingkesh.librarysystem.entity.Borrower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepo extends JpaRepository<Borrower, Long> {
    @Query("select count(b)>0 from Borrower b where b.email = :email")
    boolean findExistByEmail(@Param("email") String email);

    Page<Borrower> findAll(Specification<Borrower> spec, Pageable pageable);
}
