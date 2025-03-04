package com.lingkesh.librarysystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchListBookModel {
    private String bookId;
    private String isbn;
    private String author;
    private String title;
    private String limitContent;
    private String page;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLimitContent() {
        return limitContent;
    }

    public void setLimitContent(String limitContent) {
        this.limitContent = limitContent;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
