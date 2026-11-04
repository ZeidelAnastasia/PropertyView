package com.example.propertyview.controller;

import com.example.propertyview.api.HotelHistogramApi;
import com.example.propertyview.repository.projection.HotelCountProjection;
import com.example.propertyview.service.HotelHistogramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/histogram")
@RequiredArgsConstructor
public class HotelHistogramController implements HotelHistogramApi {

    private final HotelHistogramService hotelHistogramService;

    @Override
    @GetMapping("/{param}")
    @ResponseStatus(HttpStatus.OK)
    public List<HotelCountProjection> getHistogram(String param) {
        return hotelHistogramService.getHistogram(param);
    }
}