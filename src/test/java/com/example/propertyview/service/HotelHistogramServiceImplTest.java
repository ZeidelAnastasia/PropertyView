package com.example.propertyview.service;

import com.example.propertyview.exception.BadRequestException;
import com.example.propertyview.repository.projection.HotelCountProjection;
import com.example.propertyview.repository.HotelRepository;
import com.example.propertyview.service.impl.HotelHistogramServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelHistogramServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelHistogramServiceImpl hotelHistogramService;

    @Mock
    private HotelCountProjection projection;

    @Test
    void getHistogram_ShouldReturnHistogramByBrand() {

        List<HotelCountProjection> expected = List.of(projection);

        when(hotelRepository.countHotelsByBrand()).thenReturn(expected);

        List<HotelCountProjection> result =
                hotelHistogramService.getHistogram("brand");

        assertNotNull(result);
        assertEquals(expected, result);

        verify(hotelRepository).countHotelsByBrand();
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldReturnHistogramByCity() {

        List<HotelCountProjection> expected = List.of(projection);

        when(hotelRepository.countHotelsByCity()).thenReturn(expected);

        List<HotelCountProjection> result =
                hotelHistogramService.getHistogram("city");

        assertNotNull(result);
        assertEquals(expected, result);

        verify(hotelRepository).countHotelsByCity();
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldReturnHistogramByCountry() {

        List<HotelCountProjection> expected = List.of(projection);

        when(hotelRepository.countHotelsByCountry()).thenReturn(expected);

        List<HotelCountProjection> result =
                hotelHistogramService.getHistogram("country");

        assertNotNull(result);
        assertEquals(expected, result);

        verify(hotelRepository).countHotelsByCountry();
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldReturnHistogramByAmenities() {

        List<HotelCountProjection> expected = List.of(projection);

        when(hotelRepository.countHotelsByAmenity()).thenReturn(expected);

        List<HotelCountProjection> result =
                hotelHistogramService.getHistogram("amenities");

        assertNotNull(result);
        assertEquals(expected, result);

        verify(hotelRepository).countHotelsByAmenity();
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldIgnoreCase() {

        List<HotelCountProjection> expected = List.of(projection);

        when(hotelRepository.countHotelsByBrand()).thenReturn(expected);

        List<HotelCountProjection> result =
                hotelHistogramService.getHistogram("BRAND");

        assertEquals(expected, result);

        verify(hotelRepository).countHotelsByBrand();
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldIgnoreLeadingAndTrailingSpaces() {

        List<HotelCountProjection> expected = List.of(projection);

        when(hotelRepository.countHotelsByCity()).thenReturn(expected);

        List<HotelCountProjection> result =
                hotelHistogramService.getHistogram("  city  ");

        assertEquals(expected, result);

        verify(hotelRepository).countHotelsByCity();
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldThrowExceptionForUnsupportedParameter() {

        assertThrows(
                BadRequestException.class,
                () -> hotelHistogramService.getHistogram("name")
        );

        verifyNoInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldThrowExceptionForNullParameter() {

        assertThrows(
                BadRequestException.class,
                () -> hotelHistogramService.getHistogram(null)
        );

        verifyNoInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldThrowExceptionForBlankParameter() {

        assertThrows(
                BadRequestException.class,
                () -> hotelHistogramService.getHistogram("   ")
        );

        verifyNoInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldReturnEmptyListWhenNoDataFound() {

        when(hotelRepository.countHotelsByBrand()).thenReturn(List.of());

        List<HotelCountProjection> result =
                hotelHistogramService.getHistogram("brand");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(hotelRepository).countHotelsByBrand();
        verifyNoMoreInteractions(hotelRepository);
    }
}