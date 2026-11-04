package com.example.propertyview.service;

import com.example.propertyview.repository.projection.HotelCountProjection;

import java.util.List;

public interface HotelHistogramService {

    List<HotelCountProjection> getHistogram(String param);
}