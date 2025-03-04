package com.lingkesh.librarysystem.controller;

import com.lingkesh.librarysystem.entity.Book;
import com.lingkesh.librarysystem.entity.BorrowedBook;
import com.lingkesh.librarysystem.entity.Borrower;
import com.lingkesh.librarysystem.model.RegisterBookModel;
import com.lingkesh.librarysystem.model.ResponseModel;
import com.lingkesh.librarysystem.model.SearchListBookModel;
import com.lingkesh.librarysystem.service.BookService;
import com.lingkesh.librarysystem.service.BorrowedBookService;
import com.lingkesh.librarysystem.service.BorrowerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@Tag(name = "Books", description = "Handles book management, including registration, retrieval, searching, borrowing, returning, and tracking borrowing history.")
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    BorrowerService borrowerService;
    @Autowired
    BorrowedBookService borrowedBookService;

    // Register Book
    @RequestMapping(value = "/register", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> registerBook(@Valid @RequestBody RegisterBookModel registerBookModel) {
        ResponseModel responseModal = new ResponseModel();

        try {
            Book savedBook = bookService.registerBook(registerBookModel);

            responseModal.setCode(ResponseModel.REGISTER_BOOK_SUCCESS);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.REGISTER_BOOK_SUCCESS));
            responseModal.setObject(savedBook.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseModal);
        } catch (RuntimeException e) {
            responseModal.setCode(ResponseModel.EXCEPTION_ERROR);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.EXCEPTION_ERROR)  + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModal);
        }
    }

    // Retrieve Book Details by ID
    @RequestMapping(value = "/retrieveBookDetails/{bookId}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> retrieveBookDetailsById(@Parameter(description = "ID of the book to retrieve", example = "1") @PathVariable Long bookId) {
        ResponseModel responseModel = new ResponseModel();

        Optional<Book> checkBookExists = bookService.findExistById(bookId);
        if (checkBookExists.isPresent()) {
            Book savedBook = bookService.retrieveBookDetails(bookId);

            responseModel.setCode(ResponseModel.RETRIEVE_BOOK_DETAILS_SUCCESS);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_DETAILS_SUCCESS));
            responseModel.setObject(savedBook.toString());
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } else {
            responseModel.setCode(ResponseModel.BOOK_ID_NOT_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.BOOK_ID_NOT_FOUND) + bookId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
        }
    }

    // Retrieve List of Books
    @RequestMapping(value = "/retrieveAllBooks", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> retrieveAllBooks() {
        ResponseModel responseModel = new ResponseModel();

        List<Book> savedBooks = bookService.retrieveAllBooks();

        if (savedBooks.isEmpty()) {
            responseModel.setCode(ResponseModel.RETRIEVE_BOOK_LIST_NOT_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_LIST_NOT_FOUND));
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }

        responseModel.setCode(ResponseModel.RETRIEVE_BOOK_LIST_SUCCESS);
        responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_LIST_SUCCESS));
        responseModel.setObject(savedBooks.toString());
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    // Search for a Book
    @RequestMapping(value = "/searchBooks", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<ResponseModel> searchListOfBooks(@RequestBody SearchListBookModel searchListBookModel) {
        ResponseModel responseModel = new ResponseModel();

        List<Book> searchListBooks = bookService.searchBooks(searchListBookModel.getBookId(), searchListBookModel.getIsbn(),
                searchListBookModel.getAuthor(), searchListBookModel.getTitle(), searchListBookModel.getPage(),
                searchListBookModel.getLimitContent());

        if (searchListBooks.isEmpty()) {
            responseModel.setCode(ResponseModel.RETRIEVE_BOOK_LIST_NOT_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_LIST_NOT_FOUND));
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }

        responseModel.setCode(ResponseModel.RETRIEVE_BOOK_LIST_SUCCESS);
        responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BOOK_LIST_SUCCESS));
        responseModel.setObject(searchListBooks.toString());
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    // Borrow a book
    @RequestMapping(value = "/{borrowerId}/borrow/{bookId}", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> borrowBook(@Parameter(description = "ID of the borrower wanted to borrow the book", example = "1") @PathVariable Long borrowerId, @Parameter(description = "ID of the book to borrow", example = "1") @PathVariable Long bookId) {
        ResponseModel responseModel = new ResponseModel();

        //Check book id exist or not
        Optional<Book> checkBookIDExist = bookService.findExistById(bookId);
        if(checkBookIDExist.isPresent()){

            //Check Book Already borrowed or not with the ID
            if(borrowedBookService.checkBookAlreadyBorrowed(bookId)){
                responseModel.setCode(ResponseModel.BORROW_BOOK_FAILED_BOOK_ALREADY_BORROWED);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.BORROW_BOOK_FAILED_BOOK_ALREADY_BORROWED));
                return ResponseEntity.status(HttpStatus.OK).body(responseModel);
            }

            //Check borrower id exist or not
            Optional<Borrower> checkBorrowerIDExist = borrowerService.findExistById(borrowerId);

            if(checkBorrowerIDExist.isPresent()){
                BorrowedBook borrowedBook = borrowedBookService.borrowBook(borrowerId, bookId);

                responseModel.setCode(ResponseModel.BORROW_BOOK_SUCCESS);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.BORROW_BOOK_SUCCESS));
                responseModel.setObject(borrowedBook.toString());
                return ResponseEntity.status(HttpStatus.OK).body(responseModel);
            }else{
                responseModel.setCode(ResponseModel.BORROWER_ID_NOT_FOUND);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.BORROWER_ID_NOT_FOUND) + borrowerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
            }
        }else{
            responseModel.setCode(ResponseModel.BOOK_ID_NOT_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.BOOK_ID_NOT_FOUND) + bookId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
        }
    }

    // Return a book
    @RequestMapping(value = "/return/{bookId}", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> returnBook(@Parameter(description = "ID of the book to return the borrowed book", example = "1") @PathVariable Long bookId) {
        ResponseModel responseModel = new ResponseModel();

        //Check book id exist or not
        Optional<Book> checkBookIDExist = bookService.findExistById(bookId);
        if(checkBookIDExist.isPresent()){

            //Check Book Already borrowed or not using the ID
            if(!borrowedBookService.checkBookAlreadyBorrowed(bookId)){
                responseModel.setCode(ResponseModel.RETURN_BOOK_FAILED_BOOK_NOT_BORROW);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETURN_BOOK_FAILED_BOOK_NOT_BORROW));
                return ResponseEntity.status(HttpStatus.OK).body(responseModel);
            }

            BorrowedBook returnedBook = borrowedBookService.returnBook(bookId);

            responseModel.setCode(ResponseModel.RETURN_BOOK_SUCCESS);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETURN_BOOK_SUCCESS));
            responseModel.setObject(returnedBook.toString());
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }else{
            responseModel.setCode(ResponseModel.BOOK_ID_NOT_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.BOOK_ID_NOT_FOUND) + bookId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
        }
    }

    // Get borrowing history by Book ID
    @RequestMapping(value = "/historyBook/{bookId}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> getHistoryByBook(@Parameter(description = "ID of the book to retrieve history of borrowed book", example = "1") @PathVariable Long bookId) {
        ResponseModel responseModel = new ResponseModel();

        //Check book id exist or not
        Optional<Book> checkBookIDExist = bookService.findExistById(bookId);

        if(checkBookIDExist.isPresent()){
            List<BorrowedBook> historyList = borrowedBookService.getBorrowingHistoryByBook(bookId);

            if (historyList.isEmpty()) {
                responseModel.setCode(ResponseModel.BOOK_HISTORY_NOT_FOUND);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.BOOK_HISTORY_NOT_FOUND));
                return ResponseEntity.status(HttpStatus.OK).body(responseModel);
            }

            responseModel.setCode(ResponseModel.BOOK_HISTORY_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.BOOK_HISTORY_FOUND));
            responseModel.setObject(historyList.toString());
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }else{
            responseModel.setCode(ResponseModel.BOOK_ID_NOT_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.BOOK_ID_NOT_FOUND) + bookId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
        }
    }
}
