package com.example.propertyview.api;

import com.example.propertyview.dto.response.HotelSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
    );
}