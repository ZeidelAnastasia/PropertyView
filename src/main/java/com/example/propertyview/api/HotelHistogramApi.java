package com.example.propertyview.api;

import com.example.propertyview.repository.projection.HotelCountProjection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(
        name = "Hotel Histogram",
        description = "API for retrieving hotel statistics"
)
public interface HotelHistogramApi {

    @Operation(
            summary = "Get hotel histogram",
            description = """
                    Returns the number of hotels grouped by the specified parameter.
                    Supported parameters: brand, city, country and amenities.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Histogram retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Unsupported histogram parameter"
            )
    })
    List<HotelCountProjection> getHistogram(String param);
}