package com.lingkesh.librarysystem.service.impl;

import com.lingkesh.librarysystem.entity.Borrower;
import com.lingkesh.librarysystem.model.AddBorrowerModel;
import com.lingkesh.librarysystem.repository.specification.BorrowerList;
import com.lingkesh.librarysystem.repository.BorrowerRepo;
import com.lingkesh.librarysystem.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowerServiceImpl implements BorrowerService {
    BorrowerRepo borrowerRepo;

    @Autowired
    public BorrowerServiceImpl(BorrowerRepo borrowerRepo) {
        this.borrowerRepo = borrowerRepo;
    }

    @Override
    public Borrower registerBorrower(AddBorrowerModel addBorrowerModel) {

        //Convert Model to Entity
        Borrower borrower = new Borrower();
        borrower.setName(addBorrowerModel.getName());
        borrower.setEmail(addBorrowerModel.getEmail());

        return borrowerRepo.save(borrower);
    }

    @Override
    public Optional<Borrower> findExistById(Long borrowerId) {
        return borrowerRepo.findById(borrowerId);
    }

    @Override
    public boolean findExistByEmail(String email) {
        return borrowerRepo.findExistByEmail(email);
    }

    @Override
    public Borrower retrieveBorrowerId(Long borrowerId) {
        // Check if the borrower exists
        return borrowerRepo.findById(borrowerId).orElseThrow(() -> new RuntimeException("Borrower not found with ID: " + borrowerId));
    }

    @Override
    public Borrower updateUserDetailsBasedById(Long id, AddBorrowerModel updateBorrowerModel) {
        // Check if the borrower exists
        Borrower existingBorrower = borrowerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found with ID: " + id));

        // Compare old and new details
        boolean isUpdated = false;

        if (!existingBorrower.getName().equals(updateBorrowerModel.getName())) {
            existingBorrower.setName(updateBorrowerModel.getName());
            isUpdated = true;
        }

        if (!existingBorrower.getEmail().equals(updateBorrowerModel.getEmail())) {
            existingBorrower.setEmail(updateBorrowerModel.getEmail());
            isUpdated = true;
        }

        // Only update if changes are detected
        if (isUpdated) {
            return borrowerRepo.save(existingBorrower);
        } else {
            return existingBorrower; // No changes, return existing object
        }
    }

    @Override
    public List<Borrower> retrieveAllBorrower() {
        return borrowerRepo.findAll();
    }

    @Override
    public List<Borrower> searchBorrower(String id, String name, String email, String start, String limit) {
        int startIndex = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        Pageable pageable = PageRequest.of(startIndex, pageSize);

        Specification<Borrower> spec = Specification.where(BorrowerList.hasId(id))
                .and(BorrowerList.hasName(name))
                .and(BorrowerList.hasEmail(email));

        return borrowerRepo.findAll(spec, pageable).getContent();
    }
}
