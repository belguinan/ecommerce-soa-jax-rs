package com.example.orderservice.requests;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CheckoutRequest {
    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name is required")
    @Length(min = 2, max = 199, message = "Name must be between 2 and 199 characters")
    private String name;

    @NotNull(message = "Phone number is required")
    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotNull(message = "Address is required")
    @NotEmpty(message = "Address is required")
    @Length(min = 5, max = 199, message = "Address must be between 5 and 199 characters")
    private String address;

    @NotNull(message = "City is required")
    @NotEmpty(message = "City is required")
    @Length(min = 2, max = 199, message = "City must be between 2 and 199 characters")
    private String city;
}