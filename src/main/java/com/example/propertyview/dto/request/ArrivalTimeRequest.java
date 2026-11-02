package com.example.propertyview.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ArrivalTimeRequest(

        @NotNull(message = "Check-in time is required")
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkIn,

        @JsonFormat(pattern = "HH:mm")
        LocalTime checkOut

) {
}