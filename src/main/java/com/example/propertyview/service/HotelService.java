package com.example.propertyview.service;

import com.example.propertyview.dto.request.CreateHotelRequest;
import com.example.propertyview.dto.response.HotelDetailsResponse;
import com.example.propertyview.dto.response.HotelSummaryResponse;

import java.util.List;

public interface HotelService {

    List<HotelSummaryResponse> getAllHotels();

    HotelDetailsResponse getHotelById(Long id);

    HotelDetailsResponse createHotel(CreateHotelRequest request);

    HotelDetailsResponse addAmenities(Long hotelId, List<String> amenities);
}