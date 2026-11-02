
package com.example.propertyview.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddressRequest(

        @NotNull(message = "House number is required")
        @Positive(message = "House number must be positive")
        Integer houseNumber,

        @NotBlank(message = "Street is required")
        @Size(max = 255)
        String street,

        @NotBlank(message = "City is required")
        @Size(max = 100)
        String city,

        @NotBlank(message = "Country is required")
        @Size(max = 100)
        String country,

        @NotBlank(message = "Post code is required")
        @Size(max = 20)
        String postCode

) {
}
