package com.example.propertyview.service.impl;

import com.example.propertyview.dto.response.HotelSummaryResponse;
import com.example.propertyview.entity.Hotel;
import com.example.propertyview.mapper.HotelMapper;
import com.example.propertyview.repository.HotelRepository;
import com.example.propertyview.service.HotelSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelSearchServiceImpl implements HotelSearchService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<HotelSummaryResponse> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
    ) {
        List<Hotel> hotels = hotelRepository.searchHotels(
                name,
                brand,
                city,
                country,
                amenities
        );

        return hotels.stream()
                .map(hotelMapper::toSummaryResponse)
                .toList();
    }
}