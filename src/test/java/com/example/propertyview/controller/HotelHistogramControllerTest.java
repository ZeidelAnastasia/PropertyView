package com.example.propertyview.controller;

import com.example.propertyview.exception.BadRequestException;
import com.example.propertyview.exception.GlobalExceptionHandler;
import com.example.propertyview.repository.projection.HotelCountProjection;
import com.example.propertyview.service.HotelHistogramService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelHistogramController.class)
@Import(GlobalExceptionHandler.class)
class HotelHistogramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelHistogramService hotelHistogramService;

    @Test
    void getHistogram_WithValidParam_ShouldReturnHistogramData() throws Exception {
        String param = "city";
        HotelCountProjection item1 = createProjection("Paris", 5L);
        HotelCountProjection item2 = createProjection("Minsk", 2L);

        when(hotelHistogramService.getHistogram(param)).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/histogram/{param}", param)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].value").value("Paris"))
                .andExpect(jsonPath("$[0].totalCount").value(5))
                .andExpect(jsonPath("$[1].value").value("Minsk"))
                .andExpect(jsonPath("$[1].totalCount").value(2));

        verify(hotelHistogramService).getHistogram(param);
    }

    @Test
    void getHistogram_WhenNoData_ShouldReturnEmptyList() throws Exception {
        String param = "brand";
        when(hotelHistogramService.getHistogram(param)).thenReturn(List.of());

        mockMvc.perform(get("/histogram/{param}", param)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(hotelHistogramService).getHistogram(param);
    }

    @Test
    void getHistogram_WithInvalidParam_ShouldReturnBadRequest() throws Exception {
        String invalidParam = "unknown";
        when(hotelHistogramService.getHistogram(invalidParam))
                .thenThrow(new BadRequestException("Unsupported histogram parameter: " + invalidParam));

        mockMvc.perform(get("/histogram/{param}", invalidParam)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("Unsupported histogram parameter: " + invalidParam))
                .andExpect(jsonPath("$.instance").value("/histogram/" + invalidParam));

        verify(hotelHistogramService).getHistogram(invalidParam);
    }

    private HotelCountProjection createProjection(String value, Long totalCount) {
        return new HotelCountProjection() {
            @Override
            public String getValue() {
                return value;
            }

            @Override
            public Long getTotalCount() {
                return totalCount;
            }
        };
    }
}