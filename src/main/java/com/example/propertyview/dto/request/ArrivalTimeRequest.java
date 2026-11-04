package com.example.propertyview.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Schema(description = "Hotel check-in and check-out times")
public record ArrivalTimeRequest(

        @Schema(
                description = "Check-in time",
                example = "14:00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Check-in time is required")
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkIn,

        @Schema(
                description = "Check-out time. Defaults to 12:00 if omitted",
                example = "12:00"
        )
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkOut

) {
}