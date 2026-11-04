package com.example.propertyview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request for creating a new hotel")
public record CreateHotelRequest(

        @Schema(
                description = "Hotel name",
                example = "Hilton Paris",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Hotel name is required")
        @Size(max = 255)
        String name,

        @Schema(
                description = "Hotel description",
                example = "Luxury hotel located in the city center"
        )
        String description,

        @Schema(
                description = "Hotel brand",
                example = "Hilton"
        )
        @Size(max = 255)
        String brand,

        @Schema(
                description = "Hotel address",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Address is required")
        @Valid
        AddressRequest address,

        @Schema(
                description = "Hotel contact information",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Contacts are required")
        @Valid
        ContactsRequest contacts,

        @Schema(
                description = "Hotel check-in and check-out times. This object is optional"
        )
        @Valid
        ArrivalTimeRequest arrivalTime

) {
}