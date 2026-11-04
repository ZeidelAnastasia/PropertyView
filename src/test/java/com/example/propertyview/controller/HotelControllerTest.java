package com.example.propertyview.controller;

import com.example.propertyview.dto.request.CreateHotelRequest;
import com.example.propertyview.dto.response.AddressResponse;
import com.example.propertyview.dto.response.ArrivalTimeResponse;
import com.example.propertyview.dto.response.ContactsResponse;
import com.example.propertyview.dto.response.HotelDetailsResponse;
import com.example.propertyview.dto.response.HotelSummaryResponse;
import com.example.propertyview.exception.GlobalExceptionHandler;
import com.example.propertyview.exception.ResourceNotFoundException;
import com.example.propertyview.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
@Import(GlobalExceptionHandler.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HotelService hotelService;

    @Test
    void getAllHotels_ShouldReturnHotels() throws Exception {
        HotelSummaryResponse response = new HotelSummaryResponse(
                1L,
                "Hilton Paris",
                "Hotel in the city center",
                "12 Rue de Rivoli, Paris, 75001, France",
                "+33123456789"
        );

        mockMvc.perform(get("/hotels"))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllHotels_ShouldReturnEmptyList() throws Exception {
        when(hotelService.getAllHotels()).thenReturn(List.of());

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(hotelService).getAllHotels();
    }

    @Test
    void getHotelById_ShouldReturnHotel() throws Exception {
        HotelDetailsResponse response = createHotelDetailsResponse();

        when(hotelService.getHotelById(1L)).thenReturn(response);

        mockMvc.perform(get("/hotels/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hilton Paris"))
                .andExpect(jsonPath("$.brand").value("Hilton"))
                .andExpect(jsonPath("$.address.city").value("Paris"))
                .andExpect(jsonPath("$.contacts.phone").value("+33123456789"))
                .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00"))
                .andExpect(jsonPath("$.arrivalTime.checkOut").value("12:00"))
                .andExpect(jsonPath("$.amenities.length()").value(2));

        verify(hotelService).getHotelById(1L);
    }

    @Test
    void getHotelById_ShouldReturnNotFoundWhenHotelDoesNotExist() throws Exception {
        when(hotelService.getHotelById(999L))
                .thenThrow(new ResourceNotFoundException("Hotel with id 999 not found"));

        mockMvc.perform(get("/hotels/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(hotelService).getHotelById(999L);
    }

    @Test
    void createHotel_ShouldReturnCreatedHotel() throws Exception {
        CreateHotelRequest request = createHotelRequest();
        HotelDetailsResponse response = createHotelDetailsResponse();

        when(hotelService.createHotel(any(CreateHotelRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hilton Paris"))
                .andExpect(jsonPath("$.brand").value("Hilton"))
                .andExpect(jsonPath("$.address.city").value("Paris"));

        verify(hotelService).createHotel(any(CreateHotelRequest.class));
    }

    @Test
    void createHotel_ShouldReturnCreatedHotelWithoutOptionalFields() throws Exception {
        String request = """
                {
                    "name": "Budget Hotel",
                    "address": {
                        "houseNumber": 15,
                        "street": "Main Street",
                        "city": "Lyon",
                        "country": "France",
                        "postCode": "69001"
                    },
                    "contacts": {
                        "phone": "+33123456789",
                        "email": "hotel@example.com"
                    }
                }
                """;

        HotelDetailsResponse response = new HotelDetailsResponse(
                2L,
                "Budget Hotel",
                null,
                null,
                new AddressResponse(
                        15,
                        "Main Street",
                        "Lyon",
                        "France",
                        "69001"
                ),
                new ContactsResponse(
                        "+33123456789",
                        "hotel@example.com"
                ),
                null,
                List.of()
        );

        when(hotelService.createHotel(any(CreateHotelRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Budget Hotel"))
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.brand").doesNotExist())
                .andExpect(jsonPath("$.arrivalTime").doesNotExist())
                .andExpect(jsonPath("$.amenities").isArray());

        verify(hotelService).createHotel(any(CreateHotelRequest.class));
    }

    @Test
    void createHotel_ShouldReturnBadRequestWhenNameIsBlank() throws Exception {
        String request = """
                {
                    "name": "",
                    "address": {
                        "houseNumber": 15,
                        "street": "Main Street",
                        "city": "Lyon",
                        "country": "France",
                        "postCode": "69001"
                    },
                    "contacts": {
                        "phone": "+33123456789",
                        "email": "hotel@example.com"
                    }
                }
                """;

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(hotelService);
    }

    @Test
    void createHotel_ShouldReturnBadRequestWhenAddressIsMissing() throws Exception {
        String request = """
                {
                    "name": "Hilton Paris",
                    "contacts": {
                        "phone": "+33123456789",
                        "email": "hotel@example.com"
                    }
                }
                """;

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(hotelService);
    }

    @Test
    void addAmenities_ShouldReturnUpdatedHotel() throws Exception {
        HotelDetailsResponse response = createHotelDetailsResponse();

        when(hotelService.addAmenities(eq(1L), anyList()))
                .thenReturn(response);

        mockMvc.perform(post("/hotels/{id}/amenities", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                ["Wi-Fi", "Pool"]
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amenities.length()").value(2))
                .andExpect(jsonPath("$.amenities[0]").value("Wi-Fi"))
                .andExpect(jsonPath("$.amenities[1]").value("Pool"));

        verify(hotelService).addAmenities(eq(1L), anyList());
    }

    @Test
    void addAmenities_ShouldReturnBadRequestWhenListIsEmpty() throws Exception {
        mockMvc.perform(post("/hotels/{id}/amenities", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(hotelService);
    }

    @Test
    void addAmenities_ShouldReturnBadRequestWhenAmenityIsBlank() throws Exception {
        mockMvc.perform(post("/hotels/{id}/amenities", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                ["Wi-Fi", " "]
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(hotelService);
    }

    private CreateHotelRequest createHotelRequest() {
        return new CreateHotelRequest(
                "Hilton Paris",
                "Hotel in the city center",
                "Hilton",
                new com.example.propertyview.dto.request.AddressRequest(
                        12,
                        "Rue de Rivoli",
                        "Paris",
                        "France",
                        "75001"
                ),
                new com.example.propertyview.dto.request.ContactsRequest(
                        "+33123456789",
                        "paris@hotel.com"
                ),
                new com.example.propertyview.dto.request.ArrivalTimeRequest(
                        LocalTime.of(14, 0),
                        LocalTime.of(12, 0)
                )
        );
    }

    private HotelDetailsResponse createHotelDetailsResponse() {
        return new HotelDetailsResponse(
                1L,
                "Hilton Paris",
                "Hotel in the city center",
                "Hilton",
                new AddressResponse(
                        12,
                        "Rue de Rivoli",
                        "Paris",
                        "France",
                        "75001"
                ),
                new ContactsResponse(
                        "+33123456789",
                        "paris@hotel.com"
                ),
                new ArrivalTimeResponse(
                        LocalTime.of(14, 0),
                        LocalTime.of(12, 0)
                ),
                List.of("Wi-Fi", "Pool")
        );
    }
}