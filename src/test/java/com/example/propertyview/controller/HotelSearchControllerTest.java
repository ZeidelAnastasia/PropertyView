package com.example.propertyview.controller;

import com.example.propertyview.dto.response.HotelSummaryResponse;
import com.example.propertyview.exception.GlobalExceptionHandler;
import com.example.propertyview.service.HotelSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelSearchController.class)
@Import(GlobalExceptionHandler.class)
class HotelSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelSearchService hotelSearchService;

    @Test
    void searchHotels_WithoutParams_ShouldReturnAllHotels() throws Exception {
        HotelSummaryResponse hotel = createHotelSummaryResponse(1L, "Hilton Paris");
        when(hotelSearchService.searchHotels(null, null, null, null, null))
                .thenReturn(List.of(hotel));

        mockMvc.perform(get("/hotels/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hilton Paris"));

        verify(hotelSearchService).searchHotels(null, null, null, null, null);
    }

    @Test
    void searchHotels_WithAllParams_ShouldReturnFilteredHotels() throws Exception {
        HotelSummaryResponse hotel = createHotelSummaryResponse(1L, "Hilton Paris");
        List<String> amenities = List.of("Wi-Fi", "Pool");

        when(hotelSearchService.searchHotels("Hilton", "Hilton", "Paris", "France", amenities))
                .thenReturn(List.of(hotel));

        mockMvc.perform(get("/hotels/search")
                        .param("name", "Hilton")
                        .param("brand", "Hilton")
                        .param("city", "Paris")
                        .param("country", "France")
                        .param("amenities", "Wi-Fi", "Pool")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hilton Paris"));

        verify(hotelSearchService).searchHotels("Hilton", "Hilton", "Paris", "France", amenities);
    }

    @Test
    void searchHotels_WithAmenitiesOnly_ShouldReturnMatchingHotels() throws Exception {
        HotelSummaryResponse hotel = createHotelSummaryResponse(2L, "Marriott Rome");
        List<String> amenities = List.of("Parking", "Free breakfast");

        when(hotelSearchService.searchHotels(null, null, null, null, amenities))
                .thenReturn(List.of(hotel));

        mockMvc.perform(get("/hotels/search")
                        .param("amenities", "Parking", "Free breakfast")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Marriott Rome"));

        verify(hotelSearchService).searchHotels(null, null, null, null, amenities);
    }

    @Test
    void searchHotels_WhenNoHotelsFound_ShouldReturnEmptyList() throws Exception {
        when(hotelSearchService.searchHotels(any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/hotels/search")
                        .param("city", "NonExistentCity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(hotelSearchService).searchHotels(null, null, "NonExistentCity", null, null);
    }

    private HotelSummaryResponse createHotelSummaryResponse(Long id, String name) {
        return new HotelSummaryResponse(
                id,
                name,
                "Description for " + name,
                "Address, City, Country",
                "+123456789"
        );
    }
}