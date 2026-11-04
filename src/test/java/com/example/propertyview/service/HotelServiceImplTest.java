package com.example.propertyview.service;

import com.example.propertyview.dto.request.CreateHotelRequest;
import com.example.propertyview.dto.response.HotelDetailsResponse;
import com.example.propertyview.dto.response.HotelSummaryResponse;
import com.example.propertyview.entity.ArrivalTime;
import com.example.propertyview.entity.Hotel;
import com.example.propertyview.exception.BadRequestException;
import com.example.propertyview.exception.ResourceNotFoundException;
import com.example.propertyview.mapper.HotelMapper;
import com.example.propertyview.repository.HotelRepository;
import com.example.propertyview.service.impl.HotelServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelDetailsResponse hotelDetailsResponse;
    private HotelSummaryResponse hotelSummaryResponse;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton Paris");
        hotel.setAmenities(new ArrayList<>());

        hotelDetailsResponse = mock(HotelDetailsResponse.class);
        hotelSummaryResponse = mock(HotelSummaryResponse.class);
    }

    @Test
    void getAllHotels_shouldReturnHotelList() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(hotelMapper.toSummaryResponse(hotel))
                .thenReturn(hotelSummaryResponse);

        List<HotelSummaryResponse> result = hotelService.getAllHotels();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(hotelSummaryResponse, result.get(0));

        verify(hotelRepository).findAll();
        verify(hotelMapper).toSummaryResponse(hotel);
    }

    @Test
    void getAllHotels_shouldReturnEmptyListWhenNoHotelsExist() {
        when(hotelRepository.findAll()).thenReturn(List.of());

        List<HotelSummaryResponse> result = hotelService.getAllHotels();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(hotelRepository).findAll();
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void getHotelById_shouldReturnHotelWhenExists() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDetailsResponse(hotel))
                .thenReturn(hotelDetailsResponse);

        HotelDetailsResponse result = hotelService.getHotelById(1L);

        assertNotNull(result);
        assertEquals(hotelDetailsResponse, result);

        verify(hotelRepository).findById(1L);
        verify(hotelMapper).toDetailsResponse(hotel);
    }

    @Test
    void getHotelById_shouldThrowExceptionWhenHotelDoesNotExist() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> hotelService.getHotelById(1L)
        );

        verify(hotelRepository).findById(1L);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void createHotel_shouldCreateHotelSuccessfully() {
        CreateHotelRequest request = mock(CreateHotelRequest.class);

        when(hotelMapper.toEntity(request)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailsResponse(hotel))
                .thenReturn(hotelDetailsResponse);

        HotelDetailsResponse result = hotelService.createHotel(request);

        assertNotNull(result);
        assertEquals(hotelDetailsResponse, result);

        verify(hotelMapper).toEntity(request);
        verify(hotelRepository).save(hotel);
        verify(hotelMapper).toDetailsResponse(hotel);
    }

    @Test
    void createHotel_shouldSetDefaultCheckOutWhenNotProvided() {
        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn(LocalTime.of(14, 0));
        arrivalTime.setCheckOut(null);

        hotel.setArrivalTime(arrivalTime);

        CreateHotelRequest request = mock(CreateHotelRequest.class);

        when(hotelMapper.toEntity(request)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailsResponse(hotel))
                .thenReturn(hotelDetailsResponse);

        hotelService.createHotel(request);

        assertEquals(
                LocalTime.of(12, 0),
                hotel.getArrivalTime().getCheckOut()
        );

        verify(hotelRepository).save(hotel);
    }

    @Test
    void createHotel_shouldPreserveProvidedCheckOut() {
        LocalTime expectedCheckOut = LocalTime.of(11, 30);

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn(LocalTime.of(14, 0));
        arrivalTime.setCheckOut(expectedCheckOut);

        hotel.setArrivalTime(arrivalTime);

        CreateHotelRequest request = mock(CreateHotelRequest.class);

        when(hotelMapper.toEntity(request)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailsResponse(hotel))
                .thenReturn(hotelDetailsResponse);

        hotelService.createHotel(request);

        assertEquals(
                expectedCheckOut,
                hotel.getArrivalTime().getCheckOut()
        );

        verify(hotelRepository).save(hotel);
    }

    @Test
    void createHotel_shouldNotSetArrivalTimeWhenItIsNotProvided() {
        hotel.setArrivalTime(null);

        CreateHotelRequest request = mock(CreateHotelRequest.class);

        when(hotelMapper.toEntity(request)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailsResponse(hotel))
                .thenReturn(hotelDetailsResponse);

        assertDoesNotThrow(() -> hotelService.createHotel(request));

        assertNull(hotel.getArrivalTime());

        verify(hotelRepository).save(hotel);
    }

    @Test
    void addAmenities_shouldAddNewAmenities() {
        hotel.setAmenities(new ArrayList<>(List.of("Wi-Fi")));

        List<String> newAmenities = List.of("Parking", "Swimming pool");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailsResponse(hotel))
                .thenReturn(hotelDetailsResponse);

        HotelDetailsResponse result =
                hotelService.addAmenities(1L, newAmenities);

        assertNotNull(result);
        assertEquals(
                List.of("Wi-Fi", "Parking", "Swimming pool"),
                hotel.getAmenities()
        );

        verify(hotelRepository).findById(1L);
        verify(hotelRepository).save(hotel);
        verify(hotelMapper).toDetailsResponse(hotel);
    }

    @Test
    void addAmenities_shouldNotAddDuplicateAmenities() {
        hotel.setAmenities(new ArrayList<>(List.of("Wi-Fi", "Parking")));

        List<String> newAmenities = List.of("Wi-Fi", "Swimming pool");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailsResponse(hotel))
                .thenReturn(hotelDetailsResponse);

        hotelService.addAmenities(1L, newAmenities);

        assertEquals(
                List.of("Wi-Fi", "Parking", "Swimming pool"),
                hotel.getAmenities()
        );

        verify(hotelRepository).save(hotel);
    }

    @Test
    void addAmenities_shouldThrowExceptionWhenListIsEmpty() {
        assertThrows(
                BadRequestException.class,
                () -> hotelService.addAmenities(1L, List.of())
        );

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void addAmenities_shouldThrowExceptionWhenListIsNull() {
        assertThrows(
                BadRequestException.class,
                () -> hotelService.addAmenities(1L, null)
        );

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void addAmenities_shouldThrowExceptionWhenAmenityIsBlank() {
        List<String> amenities = List.of("Wi-Fi", " ");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        assertThrows(
                BadRequestException.class,
                () -> hotelService.addAmenities(1L, amenities)
        );

        verify(hotelRepository, never()).save(any());
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void addAmenities_shouldThrowExceptionWhenHotelDoesNotExist() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> hotelService.addAmenities(1L, List.of("Wi-Fi"))
        );

        verify(hotelRepository).findById(1L);
        verify(hotelRepository, never()).save(any());
        verifyNoInteractions(hotelMapper);
    }
}