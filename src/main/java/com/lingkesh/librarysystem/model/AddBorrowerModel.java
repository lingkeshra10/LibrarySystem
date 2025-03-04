package com.lingkesh.librarysystem.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBorrowerModel {
    @NotBlank(message = "Borrower name cannot be empty")
    //@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^.{2,50}$", message = "Borrower name must be between 2 and 50 characters")
    private String name;
    @NotBlank(message = "Borrower email cannot be empty")
    @Email(message = "Borrower email format is invalid")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddBorrowerModel() {
    }

    public AddBorrowerModel(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
