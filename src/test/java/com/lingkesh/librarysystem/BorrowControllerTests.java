package com.lingkesh.librarysystem;

import com.lingkesh.librarysystem.controller.BorrowerController;
import com.lingkesh.librarysystem.entity.Book;
import com.lingkesh.librarysystem.entity.BorrowedBook;
import com.lingkesh.librarysystem.entity.Borrower;
import com.lingkesh.librarysystem.model.AddBorrowerModel;
import com.lingkesh.librarysystem.model.ResponseModel;
import com.lingkesh.librarysystem.model.SearchListBorrowerModel;
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
public class BorrowControllerTests {
    @Mock
    private BorrowerService borrowerService;
    @Mock
    private BorrowedBookService borrowedBookService;

    @InjectMocks
    private BorrowerController borrowerController;

    private AddBorrowerModel emptyBorrowerName;
    private AddBorrowerModel minBorrowerName;
    private AddBorrowerModel maxBorrowerName;
    private AddBorrowerModel emptyBorrowerEmail;
    private AddBorrowerModel invalidBorrowerEmail;

    @BeforeEach
    void setUp() {
        emptyBorrowerName = new AddBorrowerModel("", "sundar.pichai@gmail.com");
        minBorrowerName = new AddBorrowerModel("j", "sundar.pichai@gmail.com");
        maxBorrowerName = new AddBorrowerModel("maxBorrowerNameasdasdhjhkwjewqeqkejqwjeqeqwewqenssqwe", "sundar.pichai@gmail.com");
        emptyBorrowerEmail = new AddBorrowerModel("Lingkesh", "");
        invalidBorrowerEmail = new AddBorrowerModel("Lingkesh", "lslingkesh!gmail.com");
    }

    // 📌 1. Test When Name is Empty
    @Test
    public void testAddBorrower_WhenNameIsEmpty_ShouldReturnBadRequest() {
        when(borrowerService.registerBorrower(emptyBorrowerName))
                .thenThrow(new RuntimeException("Borrower name cannot be empty"));

        ResponseEntity<ResponseModel> response = borrowerController.addBorrower(emptyBorrowerName);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Borrower name cannot be empty"));
    }

    // 📌 2. Test When Name is Too Short
    @Test
    public void testAddBorrower_WhenNameIsShort_ShouldReturnBadRequest() {
        when(borrowerService.registerBorrower(minBorrowerName))
                .thenThrow(new RuntimeException("Borrower name must be between 2 and 50 characters"));

        ResponseEntity<ResponseModel> response = borrowerController.addBorrower(minBorrowerName);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Borrower name must be between 2 and 50 characters"));
    }

    // 📌 3. Test When Name is Too Long
    @Test
    public void testAddBorrower_WhenNameIsLong_ShouldReturnBadRequest() {
        when(borrowerService.registerBorrower(maxBorrowerName))
                .thenThrow(new RuntimeException("Borrower name must be between 2 and 50 characters"));

        ResponseEntity<ResponseModel> response = borrowerController.addBorrower(maxBorrowerName);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Borrower name must be between 2 and 50 characters"));
    }

    // 📌 4. Test When Email is Empty
    @Test
    public void testAddBorrower_WhenEmailIsEmpty_ShouldReturnBadRequest() {
        when(borrowerService.registerBorrower(emptyBorrowerEmail))
                .thenThrow(new RuntimeException("Borrower email cannot be empty"));

        ResponseEntity<ResponseModel> response = borrowerController.addBorrower(emptyBorrowerEmail);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Borrower email cannot be empty"));
    }

    // 📌 5. Test When Email Format is Invalid
    @Test
    public void testAddBorrower_WhenEmailIsInvalidFormat_ShouldReturnBadRequest() {
        when(borrowerService.registerBorrower(invalidBorrowerEmail))
                .thenThrow(new RuntimeException("Borrower email format is invalid"));

        ResponseEntity<ResponseModel> response = borrowerController.addBorrower(invalidBorrowerEmail);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Borrower email format is invalid"));
    }

    // 📌 6. Test Successful Borrower Registration
    @Test
    public void testAddBorrower_Success_ShouldReturnCreatedResponse() {
        AddBorrowerModel addBorrower = new AddBorrowerModel("John Doe", "john@example.com");
        Borrower newBorrower = new Borrower(1L, "John Doe", "john@example.com");

        when(borrowerService.findExistByEmail(addBorrower.getEmail())).thenReturn(false);
        when(borrowerService.registerBorrower(addBorrower)).thenReturn(newBorrower);

        ResponseEntity<ResponseModel> response = borrowerController.addBorrower(addBorrower);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.ADD_BORROWER_SUCCESS, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.ADD_BORROWER_SUCCESS), response.getBody().getMessage());
        assertEquals(newBorrower.toString(), response.getBody().getObject());

        verify(borrowerService, times(1)).findExistByEmail(addBorrower.getEmail());
        verify(borrowerService, times(1)).registerBorrower(addBorrower);
    }

    // 📌 7. Test When Email Already Exists
    @Test
    public void testAddBorrower_WhenEmailExists_ShouldReturnBadRequest() {
        AddBorrowerModel addBorrower = new AddBorrowerModel("Alice Smith", "alice@example.com");

        when(borrowerService.findExistByEmail(addBorrower.getEmail())).thenReturn(true);

        ResponseEntity<ResponseModel> response = borrowerController.addBorrower(addBorrower);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.BORROWER_EMAIL_ALREADY_EXIST, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.BORROWER_EMAIL_ALREADY_EXIST), response.getBody().getMessage());

        verify(borrowerService, times(1)).findExistByEmail(addBorrower.getEmail());
        verify(borrowerService, never()).registerBorrower(any(AddBorrowerModel.class));
    }

    // 📌 8. Test When Exception Occurs During Registration
    @Test
    public void testAddBorrower_WhenExceptionOccurs_ShouldReturnBadRequest() {
        AddBorrowerModel addBorrower = new AddBorrowerModel("Michael Brown", "michael@example.com");

        when(borrowerService.findExistByEmail(addBorrower.getEmail())).thenReturn(false);
        when(borrowerService.registerBorrower(addBorrower))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ResponseModel> response = borrowerController.addBorrower(addBorrower);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.EXCEPTION_ERROR, response.getBody().getCode());
        assertTrue(response.getBody().getMessage().contains("Database error"));

        verify(borrowerService, times(1)).findExistByEmail(addBorrower.getEmail());
        verify(borrowerService, times(1)).registerBorrower(addBorrower);
    }

    // 📌 1. Test Successful Borrower Retrieval
    @Test
    public void testRetrieveBorrowerDetails_WhenBorrowerExists_ShouldReturnBorrowerDetails() {
        // Given
        Long borrowerId = 1L;
        Borrower borrower = new Borrower();
        borrower.setId(borrowerId);
        borrower.setName("John Doe");
        borrower.setEmail("john@example.com");

        when(borrowerService.findExistById(borrowerId)).thenReturn(Optional.of(borrower));
        when(borrowerService.retrieveBorrowerId(borrowerId)).thenReturn(borrower);

        // When
        ResponseEntity<ResponseModel> response = borrowerController.retrieveBorrowerDetails(borrowerId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.RETRIEVE_BORROWER_DETAILS_SUCCESS, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_DETAILS_SUCCESS), response.getBody().getMessage());
        assertEquals(borrower.toString(), response.getBody().getObject());

        verify(borrowerService, times(1)).findExistById(borrowerId);
        verify(borrowerService, times(1)).retrieveBorrowerId(borrowerId);
    }

    // 📌 2. Test When Borrower Not Found
    @Test
    public void testRetrieveBorrowerDetails_WhenBorrowerNotFound_ShouldReturnNotFound() {
        // Given
        Long borrowerId = 999L;

        when(borrowerService.findExistById(borrowerId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<ResponseModel> response = borrowerController.retrieveBorrowerDetails(borrowerId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.BORROWER_ID_NOT_FOUND, response.getBody().getCode());
        assertTrue(response.getBody().getMessage().contains(borrowerId.toString()));

        verify(borrowerService, times(1)).findExistById(borrowerId);
        verify(borrowerService, never()).retrieveBorrowerId(anyLong());
    }

    // 📌 1. Test Successful Borrower Update
    @Test
    public void testUpdateBorrowerDetails_Success_ShouldReturnUpdatedBorrower() {
        // Given
        Long borrowerId = 1L;
        AddBorrowerModel updateBorrower = new AddBorrowerModel();
        updateBorrower.setName("John Doe Updated");
        updateBorrower.setEmail("john.updated@example.com");

        Borrower existingBorrower = new Borrower();
        existingBorrower.setId(borrowerId);
        existingBorrower.setName("John Doe");
        existingBorrower.setEmail("john@example.com");

        Borrower updatedBorrower = new Borrower();
        updatedBorrower.setId(borrowerId);
        updatedBorrower.setName(updateBorrower.getName());
        updatedBorrower.setEmail(updateBorrower.getEmail());

        when(borrowerService.findExistById(borrowerId)).thenReturn(Optional.of(existingBorrower));
        when(borrowerService.updateUserDetailsBasedById(borrowerId, updateBorrower)).thenReturn(updatedBorrower);

        // When
        ResponseEntity<ResponseModel> response = borrowerController.updateBorrowerDetails(borrowerId, updateBorrower);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.UPDATE_BORROWER_SUCCESS, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.UPDATE_BORROWER_SUCCESS), response.getBody().getMessage());
        assertEquals(updatedBorrower.toString(), response.getBody().getObject());

        verify(borrowerService, times(1)).findExistById(borrowerId);
        verify(borrowerService, times(1)).updateUserDetailsBasedById(borrowerId, updateBorrower);
    }

    // 📌 2. Test When Borrower Not Found
    @Test
    public void testUpdateBorrowerDetails_WhenBorrowerNotFound_ShouldReturnNotFound() {
        // Given
        Long borrowerId = 999L;
        AddBorrowerModel updateBorrower = new AddBorrowerModel();
        updateBorrower.setName("Alice Smith");
        updateBorrower.setEmail("alice.updated@example.com");

        when(borrowerService.findExistById(borrowerId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<ResponseModel> response = borrowerController.updateBorrowerDetails(borrowerId, updateBorrower);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.BORROWER_ID_NOT_FOUND, response.getBody().getCode());
        assertTrue(response.getBody().getMessage().contains(borrowerId.toString()));

        verify(borrowerService, times(1)).findExistById(borrowerId);
        verify(borrowerService, never()).updateUserDetailsBasedById(anyLong(), any(AddBorrowerModel.class));
    }

    // 📌 3. Test When Exception Occurs During Update
    @Test
    public void testUpdateBorrowerDetails_WhenExceptionOccurs_ShouldReturnBadRequest() {
        // Given
        Long borrowerId = 2L;
        AddBorrowerModel updateBorrower = new AddBorrowerModel();
        updateBorrower.setName("Michael Brown");
        updateBorrower.setEmail("michael.updated@example.com");

        Borrower existingBorrower = new Borrower();
        existingBorrower.setId(borrowerId);
        existingBorrower.setName("Michael Brown");
        existingBorrower.setEmail("michael@example.com");

        when(borrowerService.findExistById(borrowerId)).thenReturn(Optional.of(existingBorrower));
        when(borrowerService.updateUserDetailsBasedById(borrowerId, updateBorrower))
                .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<ResponseModel> response = borrowerController.updateBorrowerDetails(borrowerId, updateBorrower);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.EXCEPTION_ERROR, response.getBody().getCode());
        assertTrue(response.getBody().getMessage().contains("Database error"));

        verify(borrowerService, times(1)).findExistById(borrowerId);
        verify(borrowerService, times(1)).updateUserDetailsBasedById(borrowerId, updateBorrower);
    }

    // 📌 1. Test When Borrowers Exist
    @Test
    public void testRetrieveListBorrower_WhenBorrowersExist_ShouldReturnBorrowerList() {
        // Given
        List<Borrower> borrowerList = new ArrayList<>();
        Borrower borrower1 = new Borrower(1L, "John Doe", "john@example.com");
        Borrower borrower2 = new Borrower(2L, "Alice Smith", "alice@example.com");
        borrowerList.add(borrower1);
        borrowerList.add(borrower2);

        when(borrowerService.retrieveAllBorrower()).thenReturn(borrowerList);

        // When
        ResponseEntity<ResponseModel> response = borrowerController.retrieveListBorrower();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.RETRIEVE_BORROWER_LIST_SUCCESS, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_LIST_SUCCESS), response.getBody().getMessage());
        assertEquals(borrowerList.toString(), response.getBody().getObject());

        verify(borrowerService, times(1)).retrieveAllBorrower();
    }

    // 📌 2. Test When No Borrowers Found
    @Test
    public void testRetrieveListBorrower_WhenNoBorrowers_ShouldReturnEmptyResponse() {
        // Given
        List<Borrower> emptyBorrowerList = new ArrayList<>();

        when(borrowerService.retrieveAllBorrower()).thenReturn(emptyBorrowerList);

        // When
        ResponseEntity<ResponseModel> response = borrowerController.retrieveListBorrower();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.RETRIEVE_BORROWER_LIST_NOT_FOUND, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_LIST_NOT_FOUND), response.getBody().getMessage());
        assertNull(response.getBody().getObject());

        verify(borrowerService, times(1)).retrieveAllBorrower();
    }

    // 📌 1. Test When Borrowers Found
    @Test
    public void testSearchListOfBorrower_WhenBorrowersFound_ShouldReturnBorrowerList() {
        // Given
        SearchListBorrowerModel searchRequest = new SearchListBorrowerModel();
        searchRequest.setId(String.valueOf(1L));
        searchRequest.setName("John Doe");
        searchRequest.setEmail("john@example.com");
        searchRequest.setPage(String.valueOf(1));
        searchRequest.setLimitContent(String.valueOf(10));

        List<Borrower> borrowerList = new ArrayList<>();
        Borrower borrower1 = new Borrower(1L, "John Doe", "john@example.com");
        Borrower borrower2 = new Borrower(2L, "Alice Smith", "alice@example.com");
        borrowerList.add(borrower1);
        borrowerList.add(borrower2);

        when(borrowerService.searchBorrower(
                searchRequest.getId(),
                searchRequest.getName(),
                searchRequest.getEmail(),
                searchRequest.getPage(),
                searchRequest.getLimitContent()
        )).thenReturn(borrowerList);

        // When
        ResponseEntity<ResponseModel> response = borrowerController.searchListOfBorrower(searchRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.RETRIEVE_BORROWER_LIST_SUCCESS, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_LIST_SUCCESS), response.getBody().getMessage());
        assertEquals(borrowerList.toString(), response.getBody().getObject());

        verify(borrowerService, times(1)).searchBorrower(
                searchRequest.getId(),
                searchRequest.getName(),
                searchRequest.getEmail(),
                searchRequest.getPage(),
                searchRequest.getLimitContent()
        );
    }

    // 📌 2. Test When No Borrowers Found
    @Test
    public void testSearchListOfBorrower_WhenNoBorrowersFound_ShouldReturnEmptyResponse() {
        // Given
        SearchListBorrowerModel searchRequest = new SearchListBorrowerModel();
        searchRequest.setId(String.valueOf(999L));
        searchRequest.setName("Unknown Person");

        List<Borrower> emptyBorrowerList = new ArrayList<>();

        when(borrowerService.searchBorrower(
                searchRequest.getId(),
                searchRequest.getName(),
                searchRequest.getEmail(),
                searchRequest.getPage(),
                searchRequest.getLimitContent()
        )).thenReturn(emptyBorrowerList);

        // When
        ResponseEntity<ResponseModel> response = borrowerController.searchListOfBorrower(searchRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.RETRIEVE_BORROWER_LIST_NOT_FOUND, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_LIST_NOT_FOUND), response.getBody().getMessage());
        assertNull(response.getBody().getObject());

        verify(borrowerService, times(1)).searchBorrower(
                searchRequest.getId(),
                searchRequest.getName(),
                searchRequest.getEmail(),
                searchRequest.getPage(),
                searchRequest.getLimitContent()
        );
    }

    // 📌 1. Test When Borrowing History Exists
    @Test
    public void testGetHistoryByBorrower_WhenHistoryExists_ShouldReturnHistoryList() {
        // Given
        Long borrowerId = 1L;

        Borrower borrower = new Borrower();
        borrower.setId(borrowerId);
        borrower.setName("John Doe");
        borrower.setEmail("john@example.com");

        Book book1 = new Book(1L, "Effective Java", "Joshua Bloch", "978-3-16-148410-0");
        Book book2 = new Book(2L, "Clean Code", "Robert C. Martin", "978-0-13-235088-4");

        BorrowedBook borrowedBook1 = new BorrowedBook();
        borrowedBook1.setId(101L);
        borrowedBook1.setBorrower(borrower);
        borrowedBook1.setBook(book1);
        borrowedBook1.setBorrowedDate(LocalDate.now().minusDays(10));
        borrowedBook1.setReturnedDate(LocalDate.now().minusDays(5));

        BorrowedBook borrowedBook2 = new BorrowedBook();
        borrowedBook2.setId(102L);
        borrowedBook2.setBorrower(borrower);
        borrowedBook2.setBook(book2);
        borrowedBook2.setBorrowedDate(LocalDate.now().minusDays(20));
        borrowedBook2.setReturnedDate(LocalDate.now().minusDays(15));

        List<BorrowedBook> historyList = List.of(borrowedBook1, borrowedBook2);

        when(borrowedBookService.getBorrowingHistoryByBorrower(borrowerId)).thenReturn(historyList);

        // When
        ResponseEntity<ResponseModel> response = borrowerController.getHistoryByBorrower(borrowerId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.BORROWER_HISTORY_FOUND, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.BORROWER_HISTORY_FOUND), response.getBody().getMessage());
        assertEquals(historyList.toString(), response.getBody().getObject());

        verify(borrowedBookService, times(1)).getBorrowingHistoryByBorrower(borrowerId);
    }

    // 📌 2. Test When No Borrowing History Found
    @Test
    public void testGetHistoryByBorrower_WhenNoHistory_ShouldReturnEmptyResponse() {
        // Given
        Long borrowerId = 99L;
        List<BorrowedBook> emptyHistoryList = new ArrayList<>();

        when(borrowedBookService.getBorrowingHistoryByBorrower(borrowerId)).thenReturn(emptyHistoryList);

        // When
        ResponseEntity<ResponseModel> response = borrowerController.getHistoryByBorrower(borrowerId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseModel.BORROWER_HISTORY_NOT_FOUND, response.getBody().getCode());
        assertEquals(ResponseModel.getResponseMsg(ResponseModel.BORROWER_HISTORY_NOT_FOUND), response.getBody().getMessage());
        assertNull(response.getBody().getObject());

        verify(borrowedBookService, times(1)).getBorrowingHistoryByBorrower(borrowerId);
    }
}
