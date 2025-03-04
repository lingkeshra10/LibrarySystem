package com.lingkesh.librarysystem.repository;

import com.lingkesh.librarysystem.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);
}
