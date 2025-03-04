package com.lingkesh.librarysystem.controller;

import com.lingkesh.librarysystem.entity.BorrowedBook;
import com.lingkesh.librarysystem.entity.Borrower;
import com.lingkesh.librarysystem.model.AddBorrowerModel;
import com.lingkesh.librarysystem.model.ResponseModel;
import com.lingkesh.librarysystem.model.SearchListBorrowerModel;
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
@RequestMapping("/borrower")
@Tag(name = "Borrower", description = "Manage borrower registrations, updates, and retrieval of borrower details.")
public class BorrowerController {
    @Autowired
    BorrowerService borrowerService;
    @Autowired
    BorrowedBookService borrowedBookService;

    // Add Borrower
    @RequestMapping(value = "/add", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> addBorrower(@Valid @RequestBody AddBorrowerModel addBorrower) {
        ResponseModel responseModal = new ResponseModel();

        //Check email exist or not
        boolean emailResult = borrowerService.findExistByEmail(addBorrower.getEmail());
        if(emailResult){
            responseModal.setCode(ResponseModel.BORROWER_EMAIL_ALREADY_EXIST);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.BORROWER_EMAIL_ALREADY_EXIST));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModal);
        }

        try {
            Borrower borrowerDetails = borrowerService.registerBorrower(addBorrower);

            responseModal.setCode(ResponseModel.ADD_BORROWER_SUCCESS);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.ADD_BORROWER_SUCCESS));
            responseModal.setObject(borrowerDetails.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseModal);
        } catch (RuntimeException e) {
            responseModal.setCode(ResponseModel.EXCEPTION_ERROR);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.EXCEPTION_ERROR)  + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModal);
        }
    }

    // Retrieve Borrower Details by using ID
    @RequestMapping(value = "/retrieveBorrowerId/{borrowerId}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> retrieveBorrowerDetails(@Parameter(description = "ID of the borrower that need to retrieve the details", example = "1") @PathVariable Long borrowerId) {
        ResponseModel responseModal = new ResponseModel();

        //Check borrower id exist or not
        Optional<Borrower> checkBorrowerIDExist = borrowerService.findExistById(borrowerId);
        if(checkBorrowerIDExist.isPresent()){
            //Retrieve borrower details based on id
            Borrower borrowerDetails = borrowerService.retrieveBorrowerId(borrowerId);

            responseModal.setCode(ResponseModel.RETRIEVE_BORROWER_DETAILS_SUCCESS);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_DETAILS_SUCCESS));
            responseModal.setObject(borrowerDetails.toString());
            return ResponseEntity.status(HttpStatus.OK).body(responseModal);
        }else{
            responseModal.setCode(ResponseModel.BORROWER_ID_NOT_FOUND);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.BORROWER_ID_NOT_FOUND) + borrowerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModal);
        }
    }

    // Update Borrower
    @RequestMapping(value = "/update/{borrowerId}", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<ResponseModel> updateBorrowerDetails(@Parameter(description = "ID of the borrower that need to update details", example = "1") @PathVariable Long borrowerId, @Valid @RequestBody AddBorrowerModel updateBorrower) {
        ResponseModel responseModal = new ResponseModel();

        //Check borrower id exist or not
        Optional<Borrower> borrowerIDNotFound = borrowerService.findExistById(borrowerId);

        if(borrowerIDNotFound.isPresent()){
            try {
                Borrower updatedBorrowerDetails = borrowerService.updateUserDetailsBasedById(borrowerId, updateBorrower);

                responseModal.setCode(ResponseModel.UPDATE_BORROWER_SUCCESS);
                responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.UPDATE_BORROWER_SUCCESS));
                responseModal.setObject(String.valueOf(updatedBorrowerDetails));

                return ResponseEntity.status(HttpStatus.OK).body(responseModal);
            } catch (RuntimeException e) {
                responseModal.setCode(ResponseModel.EXCEPTION_ERROR);
                responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.EXCEPTION_ERROR)  + e.getMessage());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModal);
            }
        }else{
            responseModal.setCode(ResponseModel.BORROWER_ID_NOT_FOUND);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.BORROWER_ID_NOT_FOUND) + borrowerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModal);
        }
    }

    // Retrieve List of Borrower
    @RequestMapping(value = "/retrieveBorrowerList", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> retrieveListBorrower() {
        ResponseModel responseModel = new ResponseModel();
        List<Borrower> listOfBorrower = borrowerService.retrieveAllBorrower();

        if (listOfBorrower.isEmpty()) {
            responseModel.setCode(ResponseModel.RETRIEVE_BORROWER_LIST_NOT_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_LIST_NOT_FOUND));
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }

        responseModel.setCode(ResponseModel.RETRIEVE_BORROWER_LIST_SUCCESS);
        responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_LIST_SUCCESS));
        responseModel.setObject(listOfBorrower.toString());
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    // Search List of Borrower
    @RequestMapping(value = "/searchBorrower", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<ResponseModel> searchListOfBorrower(@RequestBody SearchListBorrowerModel searchList) {
        ResponseModel responseModal = new ResponseModel();
        List<Borrower> listOfBorrower = borrowerService.searchBorrower(searchList.getId(), searchList.getName(), searchList.getEmail(), searchList.getPage(), searchList.getLimitContent());

        if (listOfBorrower.isEmpty()) {
            responseModal.setCode(ResponseModel.RETRIEVE_BORROWER_LIST_NOT_FOUND);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_LIST_NOT_FOUND));
            return ResponseEntity.status(HttpStatus.OK).body(responseModal);
        }

        responseModal.setCode(ResponseModel.RETRIEVE_BORROWER_LIST_SUCCESS);
        responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_BORROWER_LIST_SUCCESS));
        responseModal.setObject(listOfBorrower.toString());
        return ResponseEntity.status(HttpStatus.OK).body(responseModal);
    }

    // Get borrowing history by Borrower ID
    @RequestMapping(value = "/historyBorrower/{borrowerId}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> getHistoryByBorrower(@Parameter(description = "ID of the borrower that borrow book", example = "1") @PathVariable Long borrowerId) {
        ResponseModel responseModal = new ResponseModel();
        List<BorrowedBook> historyBorrowerList = borrowedBookService.getBorrowingHistoryByBorrower(borrowerId);

        if (historyBorrowerList.isEmpty()) {
            responseModal.setCode(ResponseModel.BORROWER_HISTORY_NOT_FOUND);
            responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.BORROWER_HISTORY_NOT_FOUND));
            return ResponseEntity.status(HttpStatus.OK).body(responseModal);
        }

        responseModal.setCode(ResponseModel.BORROWER_HISTORY_FOUND);
        responseModal.setMessage(ResponseModel.getResponseMsg(ResponseModel.BORROWER_HISTORY_FOUND));
        responseModal.setObject(historyBorrowerList.toString());
        return ResponseEntity.status(HttpStatus.OK).body(responseModal);
    }
}
