package com.lingkesh.librarysystem.service;

import com.lingkesh.librarysystem.entity.BorrowedBook;

import java.util.List;
import java.util.Optional;

public interface BorrowedBookService {
    boolean checkBookAlreadyBorrowed(Long bookId);

    BorrowedBook borrowBook(Long borrowerId, Long bookId);

    BorrowedBook returnBook(Long bookId);

    List<BorrowedBook> getBorrowingHistoryByBook(Long bookId);

    List<BorrowedBook> getBorrowingHistoryByBorrower(Long borrowerId);
}
