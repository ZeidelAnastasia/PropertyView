package com.example.propertyview.service.impl;

import com.example.propertyview.exception.BadRequestException;
import com.example.propertyview.repository.HotelRepository;
import com.example.propertyview.repository.projection.HotelCountProjection;
import com.example.propertyview.service.HotelHistogramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelHistogramServiceImpl implements HotelHistogramService {

    private final HotelRepository hotelRepository;

    @Override
    public List<HotelCountProjection> getHistogram(String param) {
        if (param == null || param.isBlank()) {
            throw new BadRequestException("Histogram parameter must not be blank");
        }

        String normalizedParam = param.trim().toLowerCase(Locale.ROOT);

        return switch (normalizedParam) {
            case "brand" -> hotelRepository.countHotelsByBrand();
            case "city" -> hotelRepository.countHotelsByCity();
            case "country" -> hotelRepository.countHotelsByCountry();
            case "amenities" -> hotelRepository.countHotelsByAmenity();
            default -> throw new BadRequestException(
                    "Unsupported histogram parameter: " + param
            );
        };
    }
}