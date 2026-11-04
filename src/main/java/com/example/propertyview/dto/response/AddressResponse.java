package com.example.propertyview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Hotel address information")
public record AddressResponse(

        @Schema(description = "Building number", example = "12")
        Integer houseNumber,

        @Schema(description = "Street name", example = "Rue de Rivoli")
        String street,

        @Schema(description = "City", example = "Paris")
        String city,

        @Schema(description = "Country", example = "France")
        String country,

        @Schema(description = "Postal code", example = "75001")
        String postCode

) {
}