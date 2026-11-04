package com.example.propertyview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Hotel address information")
public record AddressRequest(

        @Schema(
                description = "House number",
                example = "12",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "House number is required")
        @Positive(message = "House number must be positive")
        Integer houseNumber,

        @Schema(
                description = "Street name",
                example = "Rue de Rivoli",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Street is required")
        @Size(max = 255)
        String street,

        @Schema(
                description = "City",
                example = "Paris",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "City is required")
        @Size(max = 100)
        String city,

        @Schema(
                description = "Country",
                example = "France",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Country is required")
        @Size(max = 100)
        String country,

        @Schema(
                description = "Postal code",
                example = "75001",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Post code is required")
        @Size(max = 20)
        String postCode

) {
}