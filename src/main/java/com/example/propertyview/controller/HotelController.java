package com.example.propertyview.controller;

import com.example.propertyview.api.HotelApi;
import com.example.propertyview.dto.request.CreateHotelRequest;
import com.example.propertyview.dto.response.HotelDetailsResponse;
import com.example.propertyview.dto.response.HotelSummaryResponse;
import com.example.propertyview.service.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Validated
public class HotelController implements HotelApi {

    private final HotelService hotelService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HotelDetailsResponse getHotelById(
            @PathVariable Long id
    ) {
        return hotelService.getHotelById(id);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotelDetailsResponse createHotel(
            @Valid @RequestBody CreateHotelRequest request
    ) {
        return hotelService.createHotel(request);
    }

    @Override
    @PostMapping("/{id}/amenities")
    @ResponseStatus(HttpStatus.OK)
    public HotelDetailsResponse addAmenities(
            @PathVariable Long id,
            @Valid @NotEmpty(message = "Amenities list must not be empty")
            @RequestBody List<@NotBlank(message = "Amenity must not be blank") String> amenities
    ) {
        return hotelService.addAmenities(id, amenities);
    }
}