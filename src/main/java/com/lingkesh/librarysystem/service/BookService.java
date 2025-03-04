package com.lingkesh.librarysystem.service;

import com.lingkesh.librarysystem.entity.Book;
import com.lingkesh.librarysystem.model.RegisterBookModel;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book registerBook(RegisterBookModel registerBookModel);

    Optional<Book> findExistById(Long bookId);

    Book retrieveBookDetails(Long bookId);

    List<Book> retrieveAllBooks();

    List<Book> searchBooks(String bookId, String isbn, String author, String title, String start, String limit);
}
