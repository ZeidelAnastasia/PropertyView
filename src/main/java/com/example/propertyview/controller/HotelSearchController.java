package com.example.propertyview.controller;

import com.example.propertyview.api.HotelSearchApi;
import com.example.propertyview.dto.response.HotelSummaryResponse;
import com.example.propertyview.service.HotelSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelSearchController implements HotelSearchApi {

    private final HotelSearchService hotelSearchService;

    @Override
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<HotelSummaryResponse> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities
    ) {
        return hotelSearchService.searchHotels(
                name,
                brand,
                city,
                country,
                amenities
        );
    }
}