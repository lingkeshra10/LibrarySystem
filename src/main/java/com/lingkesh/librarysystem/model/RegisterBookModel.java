package com.lingkesh.librarysystem.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterBookModel {
    // ISBN Stands for International Standard Book Number
    // ISBN is a unique book identifier (10 or 13 digits).
    // Example of ISBN 0-306-40615-2 (ISBN-10) and 978-0-393-97950-3 (ISBN-13)
    @NotBlank(message = "ISBN cannot be empty")
    @Pattern(regexp = "^(97[89]-?[0-9]{1,5}-?[0-9]{1,7}-?[0-9]{1,7}-?[0-9X])$", message = "Invalid ISBN format")
    private String isbn;
    @NotBlank(message = "Author name cannot be empty")
    private String author;
    @NotBlank(message = "Title of Book cannot be empty")
    private String title;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
