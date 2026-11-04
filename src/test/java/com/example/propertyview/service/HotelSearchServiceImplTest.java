package com.example.propertyview.service;

import com.example.propertyview.dto.response.HotelSummaryResponse;
import com.example.propertyview.entity.Hotel;
import com.example.propertyview.mapper.HotelMapper;
import com.example.propertyview.repository.HotelRepository;
import com.example.propertyview.service.impl.HotelSearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelSearchServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelSearchServiceImpl hotelSearchService;

    private Hotel hotel;
    private HotelSummaryResponse response;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton Paris");

        response = new HotelSummaryResponse(
                1L,
                "Hilton Paris",
                "Hotel in the city center",
                "12 Rue de Rivoli, Paris, 75001, France",
                "+33123456789"
        );
    }

    @Test
    void searchHotels_ShouldReturnMatchingHotels() {

        List<Hotel> hotels = List.of(hotel);

        when(hotelRepository.searchHotels(
                "Hilton",
                "Hilton",
                "Paris",
                "France",
                List.of("Wi-Fi", "Pool")
        )).thenReturn(hotels);

        when(hotelMapper.toSummaryResponse(hotel)).thenReturn(response);

        List<HotelSummaryResponse> result = hotelSearchService.searchHotels(
                "Hilton",
                "Hilton",
                "Paris",
                "France",
                List.of("Wi-Fi", "Pool")
        );

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));

        verify(hotelRepository).searchHotels(
                "Hilton",
                "Hilton",
                "Paris",
                "France",
                List.of("Wi-Fi", "Pool")
        );
        verify(hotelMapper).toSummaryResponse(hotel);
    }

    @Test
    void searchHotels_ShouldReturnEmptyListWhenNoHotelsFound() {

        when(hotelRepository.searchHotels(
                "Unknown",
                null,
                null,
                null,
                null
        )).thenReturn(List.of());

        List<HotelSummaryResponse> result = hotelSearchService.searchHotels(
                "Unknown",
                null,
                null,
                null,
                null
        );

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(hotelRepository).searchHotels(
                "Unknown",
                null,
                null,
                null,
                null
        );
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void searchHotels_ShouldPassNullFiltersToRepository() {

        when(hotelRepository.searchHotels(
                null,
                null,
                null,
                null,
                null
        )).thenReturn(List.of(hotel));

        when(hotelMapper.toSummaryResponse(hotel)).thenReturn(response);

        List<HotelSummaryResponse> result = hotelSearchService.searchHotels(
                null,
                null,
                null,
                null,
                null
        );

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));

        verify(hotelRepository).searchHotels(
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void searchHotels_ShouldSearchByCityOnly() {

        when(hotelRepository.searchHotels(
                null,
                null,
                "Paris",
                null,
                null
        )).thenReturn(List.of(hotel));

        when(hotelMapper.toSummaryResponse(hotel)).thenReturn(response);

        List<HotelSummaryResponse> result = hotelSearchService.searchHotels(
                null,
                null,
                "Paris",
                null,
                null
        );

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));

        verify(hotelRepository).searchHotels(
                null,
                null,
                "Paris",
                null,
                null
        );
    }

    @Test
    void searchHotels_ShouldMapAllFoundHotels() {

        Hotel secondHotel = new Hotel();
        secondHotel.setId(2L);
        secondHotel.setName("Novotel Paris");

        HotelSummaryResponse secondResponse = new HotelSummaryResponse(
                2L,
                "Novotel Paris",
                "Modern hotel",
                "15 Avenue de France, Paris, 75013, France",
                "+33198765432"
        );

        when(hotelRepository.searchHotels(
                null,
                null,
                "Paris",
                null,
                null
        )).thenReturn(List.of(hotel, secondHotel));

        when(hotelMapper.toSummaryResponse(hotel)).thenReturn(response);
        when(hotelMapper.toSummaryResponse(secondHotel)).thenReturn(secondResponse);

        List<HotelSummaryResponse> result = hotelSearchService.searchHotels(
                null,
                null,
                "Paris",
                null,
                null
        );

        assertEquals(2, result.size());
        assertEquals(response, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(hotelMapper, times(1)).toSummaryResponse(hotel);
        verify(hotelMapper, times(1)).toSummaryResponse(secondHotel);
    }
}