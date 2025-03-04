package com.lingkesh.librarysystem.service;

import com.lingkesh.librarysystem.entity.Borrower;
import com.lingkesh.librarysystem.model.AddBorrowerModel;

import java.util.List;
import java.util.Optional;

public interface BorrowerService {
    Borrower registerBorrower(AddBorrowerModel addBorrowerModel);

    Optional<Borrower> findExistById(Long borrowerId);

    boolean findExistByEmail(String email);

    Borrower retrieveBorrowerId(Long borrowerId);

    Borrower updateUserDetailsBasedById(Long borrowerId, AddBorrowerModel updateBorrowerModel);

    List<Borrower> retrieveAllBorrower();

    List<Borrower> searchBorrower(String borrowerId, String name, String email, String start, String limit);
}
