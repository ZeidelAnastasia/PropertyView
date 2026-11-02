package com.example.propertyview.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactsRequest(

        @NotBlank(message = "Phone is required")
        @Size(max = 30)
        String phone,

        @Email(message = "Email must be valid")
        @Size(max = 255)
        String email

) {
}