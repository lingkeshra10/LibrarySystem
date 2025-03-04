package com.lingkesh.librarysystem;

import com.lingkesh.librarysystem.controller.BookController;
import com.lingkesh.librarysystem.entity.Book;
import com.lingkesh.librarysystem.entity.BorrowedBook;
import com.lingkesh.librarysystem.entity.Borrower;
import com.lingkesh.librarysystem.model.RegisterBookModel;
import com.lingkesh.librarysystem.model.ResponseModel;
import com.lingkesh.librarysystem.model.SearchListBookModel;
import com.lingkesh.librarysystem.service.BookService;
import com.lingkesh.librarysystem.service.BorrowedBookService;
import com.lingkesh.librarysystem.service.BorrowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTests {
	@Mock
	private BookService bookService;
	@Mock
	private BorrowerService borrowerService;
	@Mock
	private BorrowedBookService borrowedBookService;

	@InjectMocks
	private BookController bookController;

	private RegisterBookModel emptyIsbnBook;
	private RegisterBookModel invalidIsbnBook;
	private RegisterBookModel emptyTitleBook;
	private RegisterBookModel emptyAuthorBook;

	@BeforeEach
	void setUp() {
		emptyIsbnBook = new RegisterBookModel();
		emptyIsbnBook.setIsbn("");// Empty ISBN
		emptyIsbnBook.setAuthor("Clean Code");
		emptyIsbnBook.setTitle("Robert C. Martin");

		invalidIsbnBook = new RegisterBookModel();
		invalidIsbnBook.setIsbn("123456"); // Invalid ISBN format
		invalidIsbnBook.setTitle("Clean Code");
		invalidIsbnBook.setAuthor("Robert C. Martin");

		emptyTitleBook = new RegisterBookModel();
		emptyTitleBook.setIsbn("978-1-23-456789-7");
		emptyTitleBook.setTitle(""); // Empty title
		emptyTitleBook.setAuthor("Martin Fowler");

		emptyAuthorBook = new RegisterBookModel();
		emptyAuthorBook.setIsbn("978-0-321-35668-0");
		emptyAuthorBook.setTitle("Refactoring");
		emptyAuthorBook.setAuthor(""); // Empty author
	}

	// 📌 1. Test when ISBN is empty
	@Test
	public void testRegisterBook_WhenIsbnEmpty_ShouldReturnBadRequest() {
		when(bookService.registerBook(emptyIsbnBook)).thenThrow(new RuntimeException("ISBN cannot be empty"));

		ResponseEntity<ResponseModel> response = bookController.registerBook(emptyIsbnBook);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().getMessage().contains("ISBN cannot be empty"));
	}

	// 📌 2. Test when ISBN is invalid
	@Test
	public void testRegisterBook_WhenIsbnInvalid_ShouldReturnBadRequest() {
		when(bookService.registerBook(invalidIsbnBook)).thenThrow(new RuntimeException("Invalid ISBN format"));

		ResponseEntity<ResponseModel> response = bookController.registerBook(invalidIsbnBook);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().getMessage().contains("Invalid ISBN format"));
	}

	// 📌 3. Test when title is empty
	@Test
	public void testRegisterBook_WhenTitleEmpty_ShouldReturnBadRequest() {
		when(bookService.registerBook(emptyTitleBook)).thenThrow(new RuntimeException("Book Title cannot be empty"));

		ResponseEntity<ResponseModel> response = bookController.registerBook(emptyTitleBook);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().getMessage().contains("Book Title cannot be empty"));
	}

	// 📌 4. Test when author is empty
	@Test
	public void testRegisterBook_WhenAuthorEmpty_ShouldReturnBadRequest() {
		when(bookService.registerBook(emptyAuthorBook)).thenThrow(new RuntimeException("Author name cannot be empty"));

		ResponseEntity<ResponseModel> response = bookController.registerBook(emptyAuthorBook);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().getMessage().contains("Author name cannot be empty"));
	}

	// 📌 5. Test When Book is Successfully Registered
	@Test
	public void testRegisterBook_WhenSuccessful_ShouldReturnCreatedResponse() {
		// Given
		RegisterBookModel registerBookModel = new RegisterBookModel();
		registerBookModel.setTitle("Spring Boot in Action");
		registerBookModel.setAuthor("Craig Walls");
		registerBookModel.setIsbn("978-1-61729-254-5");

		Book savedBook = new Book();
		savedBook.setId(1L);
		savedBook.setTitle(registerBookModel.getTitle());
		savedBook.setAuthor(registerBookModel.getAuthor());
		savedBook.setIsbn(registerBookModel.getIsbn());

		when(bookService.registerBook(registerBookModel)).thenReturn(savedBook);

		// When
		ResponseEntity<ResponseModel> response = bookController.registerBook(registerBookModel);

		// Then
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.REGISTER_BOOK_SUCCESS, response.getBody().getCode());
		assertEquals(ResponseModel.getResponseMsg(ResponseModel.REGISTER_BOOK_SUCCESS), response.getBody().getMessage());
		assertEquals(savedBook.toString(), response.getBody().getObject());

		verify(bookService, times(1)).registerBook(registerBookModel);
	}

	// 📌 6. Test When Book Registration Fails Due to Exception
	@Test
	public void testRegisterBook_WhenExceptionOccurs_ShouldReturnBadRequestResponse() {
		// Given
		RegisterBookModel registerBookModel = new RegisterBookModel();
		registerBookModel.setTitle("Spring Boot in Action");
		registerBookModel.setAuthor("Craig Walls");
		registerBookModel.setIsbn("978-1-61729-254-5");

		when(bookService.registerBook(registerBookModel))
				.thenThrow(new RuntimeException("Database error"));

		// When
		ResponseEntity<ResponseModel> response = bookController.registerBook(registerBookModel);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.EXCEPTION_ERROR, response.getBody().getCode());
		assertTrue(response.getBody().getMessage().contains("Database error"));

		verify(bookService, times(1)).registerBook(registerBookModel);
	}

	// 📌 1. Test When Book Exists
	@Test
	public void testRetrieveBookDetailsById_WhenBookExists_ShouldReturnBookDetails() {
		Long bookId = 1L;

		Book book = new Book();
		book.setId(bookId);
		book.setTitle("Effective Java");
		book.setAuthor("Joshua Bloch");
		book.setIsbn("978-3-16-148410-0");

		when(bookService.findExistById(bookId)).thenReturn(Optional.of(book));
		when(bookService.retrieveBookDetails(bookId)).thenReturn(book);

		ResponseEntity<ResponseModel> response = bookController.retrieveBookDetailsById(bookId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.RETRIEVE_BOOK_DETAILS_SUCCESS, response.getBody().getCode());
		assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_DETAILS_SUCCESS), response.getBody().getMessage());
		assertEquals(book.toString(), response.getBody().getObject());

		verify(bookService, times(1)).findExistById(bookId);
		verify(bookService, times(1)).retrieveBookDetails(bookId);
	}

	// 📌 2. Test when book does not exist
	@Test
	public void testRetrieveBookDetailsById_WhenBookNotFound_ShouldReturnNotFound() {
		Long bookId = 99L;

		when(bookService.findExistById(bookId)).thenReturn(Optional.empty());

		ResponseEntity<ResponseModel> response = bookController.retrieveBookDetailsById(bookId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.BOOK_ID_NOT_FOUND, response.getBody().getCode());
		assertTrue(response.getBody().getMessage().contains(ResponseModel.getResponseMsg(ResponseModel.BOOK_ID_NOT_FOUND) + bookId));

		verify(bookService, times(1)).findExistById(bookId);
		verify(bookService, never()).retrieveBookDetails(bookId);
	}

	// 📌 1. Test When Books Exist
	@Test
	public void testRetrieveAllBooks_WhenBooksExist_ShouldReturnBookList() {
		List<Book> bookList = new ArrayList<>();
		Book book1 = new Book(1L, "Effective Java", "Joshua Bloch", "978-3-16-148410-0");
		Book book2 = new Book(2L, "Clean Code", "Robert C. Martin", "978-0-13-235088-4");
		bookList.add(book1);
		bookList.add(book2);

		when(bookService.retrieveAllBooks()).thenReturn(bookList);

		ResponseEntity<ResponseModel> response = bookController.retrieveAllBooks();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.RETRIEVE_BOOK_LIST_SUCCESS, response.getBody().getCode());
		assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_LIST_SUCCESS), response.getBody().getMessage());
		assertEquals(bookList.toString(), response.getBody().getObject());

		verify(bookService, times(1)).retrieveAllBooks();
	}

	// 📌 2. Test When No Books Found
	@Test
	public void testRetrieveAllBooks_WhenNoBooksExist_ShouldReturnEmptyResponse() {
		List<Book> emptyBookList = new ArrayList<>();

		when(bookService.retrieveAllBooks()).thenReturn(emptyBookList);

		ResponseEntity<ResponseModel> response = bookController.retrieveAllBooks();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.RETRIEVE_BOOK_LIST_NOT_FOUND, response.getBody().getCode());
		assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_LIST_NOT_FOUND), response.getBody().getMessage());
		assertNull(response.getBody().getObject());

		verify(bookService, times(1)).retrieveAllBooks();
	}

	// 📌 1. Test When Books Are Found
	@Test
	public void testSearchListOfBooks_WhenBooksFound_ShouldReturnBookList() {
		SearchListBookModel searchRequest = new SearchListBookModel();
		searchRequest.setIsbn("978-3-16-148410-0");
		searchRequest.setAuthor("Joshua Bloch");
		searchRequest.setTitle("Effective Java");
		searchRequest.setPage(String.valueOf(1));
		searchRequest.setLimitContent(String.valueOf(10));

		List<Book> books = new ArrayList<>();
		Book book1 = new Book();
		book1.setId(1L);
		book1.setIsbn("978-3-16-148410-0");
		book1.setTitle("Effective Java");
		book1.setAuthor("Joshua Bloch");

		books.add(book1);

		when(bookService.searchBooks(
				searchRequest.getBookId(),
				searchRequest.getIsbn(),
				searchRequest.getAuthor(),
				searchRequest.getTitle(),
				searchRequest.getPage(),
				searchRequest.getLimitContent()
		)).thenReturn(books);

		ResponseEntity<ResponseModel> response = bookController.searchListOfBooks(searchRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.RETRIEVE_BOOK_LIST_SUCCESS, response.getBody().getCode());
		assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_LIST_SUCCESS), response.getBody().getMessage());
		assertEquals(books.toString(), response.getBody().getObject());

		verify(bookService, times(1)).searchBooks(
				searchRequest.getBookId(),
				searchRequest.getIsbn(),
				searchRequest.getAuthor(),
				searchRequest.getTitle(),
				searchRequest.getPage(),
				searchRequest.getLimitContent()
		);
	}

	// 📌 2. Test When No Books Are Found
	@Test
	public void testSearchListOfBooks_WhenNoBooksFound_ShouldReturnMessage() {
		SearchListBookModel searchRequest = new SearchListBookModel();
		searchRequest.setIsbn("978-1-23-456789-7");
		searchRequest.setAuthor("Unknown Author");
		searchRequest.setTitle("Unknown Book");
		searchRequest.setPage(String.valueOf(1));
		searchRequest.setLimitContent(String.valueOf(10));

		when(bookService.searchBooks(
				searchRequest.getBookId(),
				searchRequest.getIsbn(),
				searchRequest.getAuthor(),
				searchRequest.getTitle(),
				searchRequest.getPage(),
				searchRequest.getLimitContent()
		)).thenReturn(new ArrayList<>());

		ResponseEntity<ResponseModel> response = bookController.searchListOfBooks(searchRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.RETRIEVE_BOOK_LIST_NOT_FOUND, response.getBody().getCode());
		assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_LIST_NOT_FOUND), response.getBody().getMessage());
		assertNull(response.getBody().getObject());

		verify(bookService, times(1)).searchBooks(
				searchRequest.getBookId(),
				searchRequest.getIsbn(),
				searchRequest.getAuthor(),
				searchRequest.getTitle(),
				searchRequest.getPage(),
				searchRequest.getLimitContent()
		);
	}

	// 📌 1. Test Successful Borrow
	@Test
	public void testBorrowBook_WhenSuccess_ShouldReturnBorrowedBook() {
		Long borrowerId = 1L;
		Long bookId = 1L;
		Book book = new Book(bookId, "Effective Java", "Joshua Bloch", "978-3-16-148410-0");
		Borrower borrower = new Borrower(borrowerId, "John Doe", "john.doe@gmail.com");

		when(bookService.findExistById(bookId)).thenReturn(Optional.of(book));
		when(borrowerService.findExistById(borrowerId)).thenReturn(Optional.of(borrower));
		when(borrowedBookService.checkBookAlreadyBorrowed(bookId)).thenReturn(false);

		BorrowedBook borrowedBook = new BorrowedBook(borrower, book);
		when(borrowedBookService.borrowBook(borrowerId, bookId)).thenReturn(borrowedBook);

		ResponseEntity<ResponseModel> response = bookController.borrowBook(borrowerId, bookId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.BORROW_BOOK_SUCCESS, response.getBody().getCode());
		assertEquals(ResponseModel.getResponseMsg(ResponseModel.BORROW_BOOK_SUCCESS), response.getBody().getMessage());
		assertEquals(borrowedBook.toString(), response.getBody().getObject());

		verify(bookService, times(1)).findExistById(bookId);
		verify(borrowerService, times(1)).findExistById(borrowerId);
		verify(borrowedBookService, times(1)).borrowBook(borrowerId, bookId);
	}

	// 📌 2. Test Book Not Found
	@Test
	public void testBorrowBook_WhenBookNotFound_ShouldReturnNotFound() {
		Long borrowerId = 1L;
		Long bookId = 999L;

		when(bookService.findExistById(bookId)).thenReturn(Optional.empty());

		ResponseEntity<ResponseModel> response = bookController.borrowBook(borrowerId, bookId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.BOOK_ID_NOT_FOUND, response.getBody().getCode());
		assertTrue(response.getBody().getMessage().contains(bookId.toString()));

		verify(bookService, times(1)).findExistById(bookId);
		verifyNoInteractions(borrowerService);
		verifyNoInteractions(borrowedBookService);
	}

	// 📌 3. Test Borrower Not Found
	@Test
	public void testBorrowBook_WhenBorrowerNotFound_ShouldReturnNotFound() {
		Long borrowerId = 999L;
		Long bookId = 1L;
		Book book = new Book(bookId, "Effective Java", "Joshua Bloch", "978-3-16-148410-0");

		when(bookService.findExistById(bookId)).thenReturn(Optional.of(book));
		when(borrowerService.findExistById(borrowerId)).thenReturn(Optional.empty());

		ResponseEntity<ResponseModel> response = bookController.borrowBook(borrowerId, bookId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.BORROWER_ID_NOT_FOUND, response.getBody().getCode());
		assertTrue(response.getBody().getMessage().contains(borrowerId.toString()));

		verify(bookService, times(1)).findExistById(bookId);
		verify(borrowerService, times(1)).findExistById(borrowerId);
	}

	// 📌 4. Test Book Already Borrowed
	@Test
	public void testBorrowBook_WhenBookAlreadyBorrowed_ShouldReturnFailed() {
		Long borrowerId = 1L;
		Long bookId = 1L;
		Book book = new Book(bookId, "Effective Java", "Joshua Bloch", "978-3-16-148410-0");

		when(bookService.findExistById(bookId)).thenReturn(Optional.of(book));
		when(borrowedBookService.checkBookAlreadyBorrowed(bookId)).thenReturn(true);

		ResponseEntity<ResponseModel> response = bookController.borrowBook(borrowerId, bookId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseModel.BORROW_BOOK_FAILED_BOOK_ALREADY_BORROWED, response.getBody().getCode());
		assertEquals(ResponseModel.getResponseMsg(ResponseModel.BORROW_BOOK_FAILED_BOOK_ALREADY_BORROWED), response.getBody().getMessage());

		verify(bookService, times(1)).findExistById(bookId);
		verify(borrowedBookService, times(1)).checkBookAlreadyBorrowed(bookId);
		verifyNoInteractions(borrowerService);
	}

	// 📌 1. Test Successful Book Return
	@Test
	public void testReturnBook_Success_ShouldReturnUpdatedBorrowedBook() {
		// Given
		Long bookId = 10L;

		Borrower borrower = new Borrower();
		borrower.setId(1L);
		borrower.setName("John Doe");
		borrower.setEmail("john@example.com");

		Book book = new Book();
		book.setId(bookId);
		book.setIsbn("978-3-16-148410-0");
		book.setTitle("Effective Java");
		book.setAuthor("Joshua Bloch");

		BorrowedBook borrowedBook = new BorrowedBook();
		borrowedBook.setId(1L);
		borrowedBook.setBorrower(borrower);
		borrowedBook.setBook(book);
		borrowedBook.setBorrowedDate(LocalDate.now());
		borrowedBook.setReturnedDate(LocalDate.now()); // Simulate returned book

		when(bookService.findExistById(bookId)).thenReturn(Optional.of(book));
		when(borrowedBookService.returnBook(bookId)).thenReturn(borrowedBook);

		// When
		ResponseEntity<ResponseModel> response = bookController.returnBook(bookId);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseModel.RETURN_BOOK_SUCCESS, response.getBody().getCode());
		assertEquals(borrowedBook.toString(), response.getBody().getObject());
	}

	// 📌 2. Test When Book Is Not Borrowed
	@Test
	public void testReturnBook_WhenBookNotBorrowed_ShouldReturnErrorMessage() {
		// Given
		Long bookId = 100L;
		Book mockBook = new Book();  // Create a mock book object

		when(bookService.findExistById(bookId)).thenReturn(Optional.of(mockBook));  // Mock book exists
		when(borrowedBookService.checkBookAlreadyBorrowed(bookId)).thenReturn(false);  // Mock book not borrowed

		// When
		ResponseEntity<ResponseModel> response = bookController.returnBook(bookId);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseModel.RETURN_BOOK_FAILED_BOOK_NOT_BORROW, response.getBody().getCode());
	}

	// 📌 3. Test When Book ID Does Not Exist
	@Test
	public void testReturnBook_WhenBookNotFound_ShouldReturnNotFoundStatus() {
		// Given
		Long bookId = 100L;

		when(bookService.findExistById(bookId)).thenReturn(Optional.empty());

		// When
		ResponseEntity<ResponseModel> response = bookController.returnBook(bookId);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ResponseModel.BOOK_ID_NOT_FOUND, response.getBody().getCode());
	}

	// 📌 1. Test Successful Retrieval of Borrowing History
	@Test
	public void testGetHistoryByBook_Success_ShouldReturnHistoryList() {
		// Given
		Long bookId = 5L;

		Book book = new Book();
		book.setId(bookId);
		book.setIsbn("978-3-16-148410-0");
		book.setTitle("Clean Code");
		book.setAuthor("Robert C. Martin");

		Borrower borrower1 = new Borrower();
		borrower1.setId(1L);
		borrower1.setName("Alice");

		Borrower borrower2 = new Borrower();
		borrower2.setId(2L);
		borrower2.setName("Bob");

		BorrowedBook borrowedBook1 = new BorrowedBook();
		borrowedBook1.setId(101L);
		borrowedBook1.setBorrower(borrower1);
		borrowedBook1.setBook(book);
		borrowedBook1.setBorrowedDate(LocalDate.now().minusDays(10));
		borrowedBook1.setReturnedDate(LocalDate.now().minusDays(5));

		BorrowedBook borrowedBook2 = new BorrowedBook();
		borrowedBook2.setId(102L);
		borrowedBook2.setBorrower(borrower2);
		borrowedBook2.setBook(book);
		borrowedBook2.setBorrowedDate(LocalDate.now().minusDays(20));
		borrowedBook2.setReturnedDate(LocalDate.now().minusDays(15));

		List<BorrowedBook> historyList = List.of(borrowedBook1, borrowedBook2);

		when(bookService.findExistById(bookId)).thenReturn(Optional.of(book));
		when(borrowedBookService.getBorrowingHistoryByBook(bookId)).thenReturn(historyList);

		// When
		ResponseEntity<ResponseModel> response = bookController.getHistoryByBook(bookId);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseModel.BOOK_HISTORY_FOUND, response.getBody().getCode());
		assertEquals(historyList.toString(), response.getBody().getObject());
	}

	// 📌 2. Test When No Borrowing History Exists
	@Test
	public void testGetHistoryByBook_WhenNoHistory_ShouldReturnEmptyListMessage() {
		// Given
		Long bookId = 20L;
		Book book = new Book();
		book.setId(bookId);
		book.setTitle("Refactoring");

		when(bookService.findExistById(bookId)).thenReturn(Optional.of(book));
		when(borrowedBookService.getBorrowingHistoryByBook(bookId)).thenReturn(List.of());

		// When
		ResponseEntity<ResponseModel> response = bookController.getHistoryByBook(bookId);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseModel.BOOK_HISTORY_NOT_FOUND, response.getBody().getCode());
		assertEquals(ResponseModel.getResponseMsg(ResponseModel.BOOK_HISTORY_NOT_FOUND), response.getBody().getMessage());
	}

	// 📌 3. Test When Book ID Does Not Exist
	@Test
	public void testGetHistoryByBook_WhenBookNotFound_ShouldReturnNotFoundStatus() {
		// Given
		Long bookId = 50L;

		when(bookService.findExistById(bookId)).thenReturn(Optional.empty());

		// When
		ResponseEntity<ResponseModel> response = bookController.getHistoryByBook(bookId);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ResponseModel.BOOK_ID_NOT_FOUND, response.getBody().getCode());
		assertTrue(response.getBody().getMessage().contains(bookId.toString()));
	}
}
