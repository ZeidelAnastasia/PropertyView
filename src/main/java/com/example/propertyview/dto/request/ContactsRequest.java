package com.example.propertyview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Hotel contact information")
public record ContactsRequest(

        @Schema(
                description = "Hotel contact phone number",
                example = "+33123456789",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Phone is required")
        @Size(max = 30)
        String phone,

        @Schema(
                description = "Hotel contact email address",
                example = "paris@hotel.com"
        )
        @Email(message = "Email must be valid")
        @Size(max = 255)
        String email

) {
}