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

### Book Management related API

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/book/register` | `POST` | Registers a new book. |
| `/book/retrieveBookDetails/{bookId}` | `GET` | Retrieves book details by its ID. |
| `/book/retrieveAllBooks` | `GET` | Retrieves a list of all books. |
| `/book/searchBooks` | `PUT` | Searches for books based on criteria. |
| `/book/{borrowerId}/borrow/{bookId}` | `POST` | Borrows a book for a given borrower. |
| `/book/return/{bookId}` | `POST` | Returns a borrowed book. |
| `/book/historyBook/{bookId}` | `GET` | Retrieves the borrowing history of a book. |

### Borrow Management related API

| Endpoint                          | Method | Description |
|-----------------------------------|--------|-------------|
| `/borrower/add`                            | `POST`  | Add a new borrower. Checks if the email already exists before registration. |
| `/borrower/retrieveBorrowerId/{borrowerId}` | `GET`   | Retrieve borrower details using their ID. Returns 404 if the borrower is not found. |
| `/borrower/update/{borrowerId}`            | `PUT`   | Update borrower details by ID. Returns 404 if the borrower is not found. |
| `/borrower/retrieveBorrowerList`           | `GET`   | Retrieve a list of all borrowers. Returns an empty response if no borrowers are found. |
| `/borrower/searchBorrower`                 | `PUT`   | Search for borrowers based on ID, name, email, pagination, and content limits. |
| `/borrower/historyBorrower/{borrowerId}`   | `GET`   | Retrieve borrowing history of a borrower using their ID. Returns an empty response if no history is found. |

## API Details

### Example API Register a Book
- **URL:** `/book/register`

- **Method:** `POST`

- **Request Body:**
  ```json
  {
    "title": "Book Title",
    "author": "Author Name",
    "isbn": "123456789",
    "pages": 250
  }

- **Response:**
  ```json
  {
    "code": "10010",
    "message": "Register Book Success",
    "object": "{book details}"
  }

## JUnit Test Cases

### JUnit 5 tests are implemented to validate API functionality.

✅ Retrieve Borrower Details
✔️ testRetrieveBorrowerDetails_WhenBorrowerExists_ShouldReturnBorrowerDetails
✔️ testRetrieveBorrowerDetails_WhenBorrowerNotFound_ShouldReturnNotFound

✅ Update Borrower Details
✔️ testUpdateBorrowerDetails_Success_ShouldReturnUpdatedBorrower
✔️ testUpdateBorrowerDetails_WhenBorrowerNotFound_ShouldReturnNotFound
✔️ testUpdateBorrowerDetails_WhenExceptionOccurs_ShouldReturnBadRequest

Mocking with Mockito
when(borrowerService.findExistById(anyLong())).thenReturn(Optional.of(borrower))
verify(borrowerService, times(1)).updateUserDetailsBasedById(anyLong(), any(AddBorrowerModel.class))

## GitHub Actions CI/CD Pipeline Breakdown

This GitHub Actions CI/CD pipeline automates the build, test, and deployment process for your Spring Boot Library System. It ensures that every code change is automatically built, tested, and deployed as a Docker image to Docker Hub.

1. Trigger Conditions
  ```json
  on:
  push:
    branches:
      - main
      - develop
  pull_request:
    branches:
      - main
  ```
### What This Does?
- The pipeline runs whenever code is pushed to the main or develop branch.
- It also runs when a pull request is opened against main, ensuring that new changes are verified before merging.

2. Job: build
  ```json
  jobs:
    build:
      runs-on: ubuntu-latest
  ```
### What This Does?
- Defines a job named build that runs on Ubuntu (latest version).

3. Checkout Code
  ```json
  steps:
    - name: Checkout Code
      uses: actions/checkout@v3
  ```
### What This Does?
- Pulls the latest code from GitHub so the pipeline can work with it.
   
4. Set Up Java 17
  ```json
  - name: Set up JDK 17
    uses: actions/setup-java@v3
    with:
      distribution: 'temurin'
      java-version: '17'
  ```
### What This Does?
- Installs and configures Java 17 (Temurin distribution) for building the project.

5. Build the Project Using Maven
  ```json
  - name: Build with Maven
    run: mvn clean package
  ```
### What This Does?
- Runs mvn clean package to compile, test, and package the Spring Boot application into a JAR file.

6. Build Docker Image
  ```json
  - name: Build Docker Image
    run: docker build -t lingkeshra10/librarysystem:latest .
  ```
### What This Does?
- Uses the Dockerfile to build a Docker image of the application.
- The image is tagged as lingkeshra10/librarysystem:latest.

7. Push Docker Image to Docker Hub
  ```json
  - name: Push to Docker Hub
    run: |
      echo "${{ secrets.DOCKER_PASSWORD }}" | docker login -u "${{ secrets.DOCKER_USERNAME }}" --password-stdin
      docker tag lingkeshra10/librarysystem:latest lingkeshra10/librarysystem:v1
      docker push lingkeshra10/librarysystem:v1
  ```
### What This Does?
- Logs into Docker Hub using GitHub Secrets (DOCKER_USERNAME and DOCKER_PASSWORD).
- Tags the image as v1.
- Pushes it to Docker Hub under your repository (lingkeshra10/librarysystem).


## API Documentation with SpringDoc

The project uses springdoc-openapi-starter-webmvc-ui (v2.1.0) to generate API documentation.

📌 OpenAPI is configured in ApiConfig.java
📌 API docs are available at http://localhost:8080/swagger-ui.html


## Future Enhancements

🚀 To Do:

- Add JWT authentication for securing endpoints.
- Improve pagination & search filters for borrower retrieval.
- Implement circuit breaker (Resilience4J) for API reliability.
