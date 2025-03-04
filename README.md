# Library Management System

## Overview

This project is a Spring Boot application for managing book borrower details and process of borrowing book and returning book. It includes RESTful APIs for retrieving and updating borrower information, handle book borrowing and returning process and a batch job for processing text files, and implements best practices in OOP, design patterns, and RESTful API standards.

The project also integrates SpringDoc for API documentation, JUnit for testing, and GitHub Actions for CI/CD.

Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Data JPA (for database interactions)
- MySQL (as database)
- JUnit 5 (for testing)
- Mockito (for mocking dependencies in tests)
- SpringDoc OpenAPI (for API documentation)
- Docker & Docker Compose (for containerization)
- GitHub Actions (for CI/CD)

## API Endpoints

This project follows RESTful API standards with proper response handling.

# Book Management API

This API provides functionality for managing books, including registering, retrieving, searching, borrowing, and returning books.

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/register` | `POST` | Registers a new book. |
| `/retrieveBookDetails/{bookId}` | `GET` | Retrieves book details by its ID. |
| `/retrieveAllBooks` | `GET` | Retrieves a list of all books. |
| `/searchBooks` | `PUT` | Searches for books based on criteria. |
| `/{borrowerId}/borrow/{bookId}` | `POST` | Borrows a book for a given borrower. |
| `/return/{bookId}` | `POST` | Returns a borrowed book. |
| `/historyBook/{bookId}` | `GET` | Retrieves the borrowing history of a book. |

## API Details

### 1. Register a Book
- **URL:** `/register`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "title": "Book Title",
    "author": "Author Name",
    "isbn": "123456789",
    "pages": 250
  }

