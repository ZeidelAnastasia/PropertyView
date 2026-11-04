package com.example.propertyview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Short hotel information returned in hotel lists and search results")
public record HotelSummaryResponse(

        @Schema(description = "Unique hotel identifier", example = "1")
        Long id,

        @Schema(description = "Hotel name", example = "Hilton Paris")
        String name,

        @Schema(
                description = "Hotel description",
                example = "A luxury hotel located in the heart of Paris"
        )
        String description,

        @Schema(
                description = "Formatted hotel address",
                example = "12 Rue de Rivoli, Paris, 75001, France"
        )
        String address,

        @Schema(description = "Hotel phone number", example = "+33123456789")
        String phone

) {
}