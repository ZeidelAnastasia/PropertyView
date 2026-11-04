package com.example.propertyview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Detailed information about a hotel")
public record HotelDetailsResponse(

        @Schema(description = "Unique hotel identifier", example = "1")
        Long id,

        @Schema(description = "Hotel name", example = "Hilton Paris")
        String name,

        @Schema(
                description = "Hotel description",
                example = "A luxury hotel located in the heart of Paris"
        )
        String description,

        @Schema(description = "Hotel brand", example = "Hilton")
        String brand,

        @Schema(description = "Hotel address")
        AddressResponse address,

        @Schema(description = "Hotel contact information")
        ContactsResponse contacts,

        @Schema(description = "Hotel check-in and check-out times")
        ArrivalTimeResponse arrivalTime,

        @Schema(
                description = "List of amenities available at the hotel",
                example = "[\"Wi-Fi\", \"Swimming pool\", \"Parking\", \"Restaurant\"]"
        )
        List<String> amenities

) {
}