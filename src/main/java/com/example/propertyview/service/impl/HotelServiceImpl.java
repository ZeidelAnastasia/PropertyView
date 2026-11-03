package com.example.propertyview.service.impl;

import com.example.propertyview.dto.request.CreateHotelRequest;
import com.example.propertyview.dto.response.HotelDetailsResponse;
import com.example.propertyview.dto.response.HotelSummaryResponse;
import com.example.propertyview.entity.Hotel;
import com.example.propertyview.exception.BadRequestException;
import com.example.propertyview.exception.ResourceNotFoundException;
import com.example.propertyview.mapper.HotelMapper;
import com.example.propertyview.repository.HotelRepository;
import com.example.propertyview.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private static final LocalTime DEFAULT_CHECK_OUT = LocalTime.of(12, 0);

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDetailsResponse getHotelById(Long id) {
        Hotel hotel = findHotelById(id);

        return hotelMapper.toDetailsResponse(hotel);
    }

    @Override
    @Transactional
    public HotelDetailsResponse createHotel(CreateHotelRequest request) {
        Hotel hotel = hotelMapper.toEntity(request);

        if (hotel.getArrivalTime() != null
                && hotel.getArrivalTime().getCheckOut() == null) {
            hotel.getArrivalTime().setCheckOut(DEFAULT_CHECK_OUT);
        }

        Hotel savedHotel = hotelRepository.save(hotel);

        return hotelMapper.toDetailsResponse(savedHotel);
    }

    @Override
    @Transactional
    public HotelDetailsResponse addAmenities(
            Long hotelId,
            List<String> amenities
    ) {
        if (amenities == null || amenities.isEmpty()) {
            throw new BadRequestException(
                    "Amenities list must not be empty"
            );
        }

        Hotel hotel = findHotelById(hotelId);

        List<String> updatedAmenities = new ArrayList<>(
                hotel.getAmenities()
        );

        for (String amenity : amenities) {
            if (amenity == null || amenity.isBlank()) {
                throw new BadRequestException(
                        "Amenity must not be blank"
                );
            }

            if (!updatedAmenities.contains(amenity)) {
                updatedAmenities.add(amenity);
            }
        }

        hotel.setAmenities(updatedAmenities);

        Hotel savedHotel = hotelRepository.save(hotel);

        return hotelMapper.toDetailsResponse(savedHotel);
    }

    @Transactional(readOnly = true)
    private Hotel findHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Hotel with id " + id + " not found"
                ));
    }
}