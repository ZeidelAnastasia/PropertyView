package com.example.propertyview.api;

import com.example.propertyview.dto.response.HotelSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(
        name = "Hotel Search",
        description = "API for searching and filtering hotels"
)
public interface HotelSearchApi {

    @Operation(
            summary = "Search hotels",
            description = """
                    Searches for hotels using optional filters:
                    name, brand, city, country and amenities.
                    All specified filters are applied simultaneously.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters"
            )
    })
    List<HotelSummaryResponse> searchHotels(
            @Parameter(description = "Hotel name (partial match)")
            @RequestParam(required = false)
            String name,

            @Parameter(description = "Hotel brand (partial match)")
            @RequestParam(required = false)
            String brand,

            @Parameter(description = "City name (partial match)")
            @RequestParam(required = false)
            String city,

            @Parameter(description = "Country name (partial match)")
            @RequestParam(required = false)
            String country,

            @Parameter(description = "List of amenities; hotel must contain all specified amenities")
            @RequestParam(required = false)
            List<String> amenities
    );
}