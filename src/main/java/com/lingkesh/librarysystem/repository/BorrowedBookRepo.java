package com.lingkesh.librarysystem.repository;

import com.lingkesh.librarysystem.entity.BorrowedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface BorrowedBookRepo extends JpaRepository<BorrowedBook, Long> {
    Optional<BorrowedBook> findByBookIdAndReturnedDateIsNull(Long bookId);

    // Get history of a specific book
    List<BorrowedBook> findByBookIdOrderByBorrowedDateDesc(Long bookId);

    // Get borrowing history of a borrower
    List<BorrowedBook> findByBorrowerIdOrderByBorrowedDateDesc(Long borrowerId);
}
