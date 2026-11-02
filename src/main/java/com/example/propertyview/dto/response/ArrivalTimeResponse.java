package com.example.propertyview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ArrivalTimeResponse(

        @JsonFormat(pattern = "HH:mm")
        LocalTime checkIn,

        @JsonFormat(pattern = "HH:mm")
        LocalTime checkOut
) {
}