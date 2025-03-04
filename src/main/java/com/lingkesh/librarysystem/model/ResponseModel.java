package com.lingkesh.librarysystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel {
    int code;
    String message;
    String object;

    public final static int ADD_BORROWER_SUCCESS = 10001;
    public final static int UPDATE_BORROWER_SUCCESS = 10002;
    public final static int RETRIEVE_BORROWER_DETAILS_SUCCESS = 10003;
    public final static int RETRIEVE_BORROWER_LIST_SUCCESS = 10004;
    public final static int RETRIEVE_BORROWER_LIST_NOT_FOUND = 10005;

    public final static int BORROWER_ID_NOT_FOUND = 10006;
    public final static int BORROWER_EMAIL_ALREADY_EXIST = 10007;
    public final static int BORROWER_HISTORY_NOT_FOUND = 10008;
    public final static int BORROWER_HISTORY_FOUND = 10009;
    public final static int REGISTER_BOOK_SUCCESS = 10010;

    public final static int RETRIEVE_BOOK_DETAILS_SUCCESS = 10011;
    public final static int RETRIEVE_BOOK_LIST_SUCCESS = 10012;
    public final static int RETRIEVE_BOOK_LIST_NOT_FOUND = 10013;
    public final static int BOOK_ID_NOT_FOUND = 10014;
    public final static int BORROW_BOOK_SUCCESS = 10015;

    public final static int BORROW_BOOK_FAILED_BOOK_ALREADY_BORROWED = 10016;
    public final static int RETURN_BOOK_SUCCESS = 10017;
    public final static int BOOK_HISTORY_FOUND = 10018;
    public final static int BOOK_HISTORY_NOT_FOUND = 10019;
    public final static int RETURN_BOOK_FAILED_BOOK_NOT_BORROW = 10020;

    public final static int EXCEPTION_ERROR = 500;

    public static String getResponseMsg(int code){
        return switch (code) {
            case ADD_BORROWER_SUCCESS -> "Add Borrower Success";
            case UPDATE_BORROWER_SUCCESS -> "Update Borrower Success";
            case RETRIEVE_BORROWER_DETAILS_SUCCESS -> "Retrieve Borrower Details Success";
            case RETRIEVE_BORROWER_LIST_SUCCESS -> "Retrieve Borrower List Successful";
            case RETRIEVE_BORROWER_LIST_NOT_FOUND -> "No List of Borrower Found";

            case BORROWER_ID_NOT_FOUND -> "Borrower ID is not found: ";
            case BORROWER_EMAIL_ALREADY_EXIST -> "Borrower Email is already exist";
            case BORROWER_HISTORY_NOT_FOUND -> "No borrower history found.";
            case BORROWER_HISTORY_FOUND -> "Borrower history found";
            case REGISTER_BOOK_SUCCESS -> "Register Book Success";

            case RETRIEVE_BOOK_DETAILS_SUCCESS -> "Retrieve Book Details Success";
            case RETRIEVE_BOOK_LIST_SUCCESS -> "Retrieve Book List Successful";
            case RETRIEVE_BOOK_LIST_NOT_FOUND -> "No List of Book Found";
            case BOOK_ID_NOT_FOUND -> "Book ID is not found";
            case BORROW_BOOK_SUCCESS -> "Successfully registered to borrow a book";

            case BORROW_BOOK_FAILED_BOOK_ALREADY_BORROWED -> "Borrow book failed because book already borrowed";
            case RETURN_BOOK_SUCCESS -> "Successfully return a book";
            case BOOK_HISTORY_FOUND -> "History of this book found";
            case BOOK_HISTORY_NOT_FOUND -> "History of this book not found";
            case RETURN_BOOK_FAILED_BOOK_NOT_BORROW -> "The book is not currently borrowed";

            case EXCEPTION_ERROR -> "Exception Error: ";
            default -> "";
        };
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
