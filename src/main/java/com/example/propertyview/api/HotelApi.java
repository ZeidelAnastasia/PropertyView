package com.example.propertyview.api;

import com.example.propertyview.dto.request.CreateHotelRequest;
import com.example.propertyview.dto.response.HotelDetailsResponse;
import com.example.propertyview.dto.response.HotelSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(
        name = "Hotels",
        description = "API for managing hotels"
)
public interface HotelApi {

    @Operation(
            summary = "Get all hotels",
            description = "Returns a list of all hotels with summary information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Hotels retrieved successfully"
    )
    List<HotelSummaryResponse> getAllHotels();

    @Operation(
            summary = "Get hotel by ID",
            description = "Returns detailed information about a hotel by its identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Hotel found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found"
            )
    })
    HotelDetailsResponse getHotelById(Long id);

    @Operation(
            summary = "Create a new hotel",
            description = "Creates a hotel with address, contacts and optional arrival time"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Hotel created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            )
    })
    HotelDetailsResponse createHotel(CreateHotelRequest request);

    @Operation(
            summary = "Add amenities to a hotel",
            description = "Adds a list of amenities to an existing hotel"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Amenities added successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid amenities list"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found"
            )
    })
    HotelDetailsResponse addAmenities(Long id, List<String> amenities);
}