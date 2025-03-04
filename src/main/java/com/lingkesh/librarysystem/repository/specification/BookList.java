package com.lingkesh.librarysystem.repository.specification;

import com.lingkesh.librarysystem.entity.Book;
import com.lingkesh.librarysystem.entity.Borrower;
import org.springframework.data.jpa.domain.Specification;

public class BookList {

    public static Specification<Book> hasId(String id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    public static Specification<Book> hasIsbn(String isbn) {
        return (root, query, criteriaBuilder) -> {
            if (isbn == null || isbn.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isbn"), isbn);
        };
    }

    public static Specification<Book> hasAuthor(String author) {
        return (root, query, criteriaBuilder) -> {
            if (author == null || author.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("author"), author);
        };
    }

    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("title"), title);
        };
    }
}
