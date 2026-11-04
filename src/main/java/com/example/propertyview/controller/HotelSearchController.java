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
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
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