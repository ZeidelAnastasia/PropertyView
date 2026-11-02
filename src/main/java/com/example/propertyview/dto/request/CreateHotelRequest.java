
package com.example.propertyview.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record CreateHotelRequest(

        @NotBlank(message = "Hotel name is required")
        @Size(max = 255)
        String name,

        String description,

        @Size(max = 255)
        String brand,

        @NotNull(message = "Address is required")
        @Valid
        AddressRequest address,

        @NotNull(message = "Contacts are required")
        @Valid
        ContactsRequest contacts,

        @Valid
        ArrivalTimeRequest arrivalTime

) {
}
