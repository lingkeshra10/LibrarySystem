package com.lingkesh.librarysystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class BorrowedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Borrower borrower;
    @ManyToOne
    private Book book;
    private LocalDate borrowedDate;
    private LocalDate returnedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public BorrowedBook() {
    }

    public BorrowedBook(Borrower borrower, Book book) {
        this.borrower = borrower;
        this.book = book;
    }

    @Override
    public String toString() {
        return "BorrowedBook{" +
                "id=" + id +
                ", borrower=" + borrower +
                ", book=" + book +
                ", borrowedDate=" + borrowedDate +
                ", returnedDate=" + returnedDate +
                '}';
    }
}