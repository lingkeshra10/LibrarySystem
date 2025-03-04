package com.lingkesh.librarysystem.service.impl;

import com.lingkesh.librarysystem.entity.Book;
import com.lingkesh.librarysystem.entity.BorrowedBook;
import com.lingkesh.librarysystem.entity.Borrower;
import com.lingkesh.librarysystem.repository.BookRepo;
import com.lingkesh.librarysystem.repository.BorrowedBookRepo;
import com.lingkesh.librarysystem.repository.BorrowerRepo;
import com.lingkesh.librarysystem.service.BorrowedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowedBookImpl implements BorrowedBookService {
    BookRepo bookRepo;
    BorrowerRepo borrowerRepo;
    BorrowedBookRepo borrowedBookRepo;

    @Autowired
    public BorrowedBookImpl(BookRepo bookRepo, BorrowerRepo borrowerRepo, BorrowedBookRepo borrowedBookRepo) {
        this.bookRepo = bookRepo;
        this.borrowerRepo = borrowerRepo;
        this.borrowedBookRepo = borrowedBookRepo;
    }

    @Override
    public boolean checkBookAlreadyBorrowed(Long bookId) {
        return borrowedBookRepo.findByBookIdAndReturnedDateIsNull(bookId).isPresent();
    }

    // Borrow a Book
    @Override
    public BorrowedBook borrowBook(Long borrowerId, Long bookId) {
        // Check if borrower exists
        Borrower borrower = borrowerRepo.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found with ID: " + borrowerId));

        // Check if book exists
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        // Check if the book is already borrowed
//        if (borrowedBookRepo.findByBookIdAndReturnedDateIsNull(bookId).isPresent()) {
//            throw new RuntimeException("Book is already borrowed and has not been returned.");
//        }

        // Save the borrowing record
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBorrower(borrower);
        borrowedBook.setBook(book);
        borrowedBook.setBorrowedDate(LocalDate.now());

        return borrowedBookRepo.save(borrowedBook);
    }

    @Override
    public BorrowedBook returnBook(Long bookId) {
        // Find active borrow entry
        BorrowedBook borrowedBook = borrowedBookRepo.findByBookIdAndReturnedDateIsNull(bookId)
                .orElseThrow(() -> new RuntimeException("This book is not currently borrowed."));

        // Update return date
        borrowedBook.setReturnedDate(LocalDate.now());

        return borrowedBookRepo.save(borrowedBook);
    }

    @Override
    public List<BorrowedBook> getBorrowingHistoryByBook(Long bookId) {
        return borrowedBookRepo.findByBookIdOrderByBorrowedDateDesc(bookId);
    }

    @Override
    public List<BorrowedBook> getBorrowingHistoryByBorrower(Long borrowerId) {
        return borrowedBookRepo.findByBorrowerIdOrderByBorrowedDateDesc(borrowerId);
    }
}