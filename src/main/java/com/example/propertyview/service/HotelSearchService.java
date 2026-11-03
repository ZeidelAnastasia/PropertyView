package com.example.propertyview.service;

import com.example.propertyview.dto.response.HotelSummaryResponse;

import java.util.List;

public interface HotelSearchService {

    List<HotelSummaryResponse> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
    );
}