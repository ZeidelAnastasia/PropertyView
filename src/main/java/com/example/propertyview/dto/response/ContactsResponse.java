package com.example.propertyview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Hotel contact information")
public record ContactsResponse(

        @Schema(description = "Hotel phone number", example = "+33123456789")
        String phone,

        @Schema(description = "Hotel email address", example = "paris@hotel.com")
        String email

) {
}