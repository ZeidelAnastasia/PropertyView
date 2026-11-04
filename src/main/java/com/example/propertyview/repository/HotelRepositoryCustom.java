package com.example.propertyview.repository;

import com.example.propertyview.entity.Hotel;
import com.example.propertyview.repository.projection.HotelCountProjection;

import java.util.List;

public interface HotelRepositoryCustom {

    List<Hotel> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
    );

    List<HotelCountProjection> countHotelsByBrand();

    List<HotelCountProjection> countHotelsByCity();

    List<HotelCountProjection> countHotelsByCountry();

    List<HotelCountProjection> countHotelsByAmenity();
}