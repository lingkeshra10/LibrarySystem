package com.lingkesh.librarysystem.service.impl;

import com.lingkesh.librarysystem.entity.Book;
import com.lingkesh.librarysystem.model.RegisterBookModel;
import com.lingkesh.librarysystem.repository.specification.BookList;
import com.lingkesh.librarysystem.repository.BookRepo;
import com.lingkesh.librarysystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    BookRepo bookRepo;

    @Autowired
    public BookServiceImpl(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public Book registerBook(RegisterBookModel registerBookModel) {
        // Convert Model to Entity
        Book book = new Book();
        book.setIsbn(registerBookModel.getIsbn());
        book.setTitle(registerBookModel.getTitle());
        book.setAuthor(registerBookModel.getAuthor());

        // Save book in database
        return bookRepo.save(book);
    }

    @Override
    public Optional<Book> findExistById(Long bookId) {
        return bookRepo.findById(bookId);
    }

    @Override
    public Book retrieveBookDetails(Long bookId) {
        return bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));
    }

    @Override
    public List<Book> retrieveAllBooks() {
        return bookRepo.findAll();
    }

    @Override
    public List<Book> searchBooks(String bookId, String isbn, String author, String title, String start, String limit) {
        int startIndex = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        Pageable pageable = PageRequest.of(startIndex, pageSize);

        Specification<Book> spec = Specification.where(BookList.hasId(bookId))
                .and(BookList.hasIsbn(isbn))
                .and(BookList.hasAuthor(author))
                .and(BookList.hasTitle(title));

        return bookRepo.findAll(spec, pageable).getContent();
    }
}
