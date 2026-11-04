package com.example.propertyview.repository;

import com.example.propertyview.entity.Address;
import com.example.propertyview.entity.Contacts;
import com.example.propertyview.entity.Hotel;
import com.example.propertyview.repository.projection.HotelCountProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        hotelRepository.deleteAll();

        hotelRepository.saveAllAndFlush(List.of(
                createHotel(
                        "Hilton Paris",
                        "Hilton",
                        "Paris",
                        "France",
                        List.of("Wi-Fi", "Pool", "Restaurant")
                ),
                createHotel(
                        "Hilton Lyon",
                        "Hilton",
                        "Lyon",
                        "France",
                        List.of("Wi-Fi", "Restaurant")
                ),
                createHotel(
                        "Marriott London",
                        "Marriott",
                        "London",
                        "United Kingdom",
                        List.of("Wi-Fi", "Pool", "Parking")
                ),
                createHotel(
                        "Novotel Paris",
                        "Novotel",
                        "Paris",
                        "France",
                        List.of("Wi-Fi", "Parking")
                )
        ));
    }

    @Test
    void searchHotels_ShouldReturnAllHotelsWhenNoFiltersProvided() {
        List<Hotel> result = hotelRepository.searchHotels(
                null,
                null,
                null,
                null,
                null
        );

        assertEquals(4, result.size());
    }

    @Test
    void searchHotels_ShouldFilterByName() {
        List<Hotel> result = hotelRepository.searchHotels(
                "Hilton",
                null,
                null,
                null,
                null
        );

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(hotel -> hotel.getName().contains("Hilton")));
    }

    @Test
    void searchHotels_ShouldFilterByBrand() {
        List<Hotel> result = hotelRepository.searchHotels(
                null,
                "Marriott",
                null,
                null,
                null
        );

        assertEquals(1, result.size());
        assertEquals("Marriott London", result.get(0).getName());
    }

    @Test
    void searchHotels_ShouldFilterByCity() {
        List<Hotel> result = hotelRepository.searchHotels(
                null,
                null,
                "Paris",
                null,
                null
        );

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(hotel -> hotel.getAddress().getCity().equals("Paris")));
    }

    @Test
    void searchHotels_ShouldFilterByCountry() {
        List<Hotel> result = hotelRepository.searchHotels(
                null,
                null,
                null,
                "France",
                null
        );

        assertEquals(3, result.size());
        assertTrue(result.stream()
                .allMatch(hotel -> hotel.getAddress().getCountry().equals("France")));
    }

    @Test
    void searchHotels_ShouldIgnoreCase() {
        List<Hotel> result = hotelRepository.searchHotels(
                "HILTON",
                null,
                null,
                null,
                null
        );

        assertEquals(2, result.size());
    }

    @Test
    void searchHotels_ShouldFindHotelsWithAllRequestedAmenities() {
        List<Hotel> result = hotelRepository.searchHotels(
                null,
                null,
                null,
                null,
                List.of("Wi-Fi", "Pool")
        );

        assertEquals(2, result.size());

        assertTrue(result.stream().allMatch(hotel ->
                hotel.getAmenities().containsAll(List.of("Wi-Fi", "Pool"))
        ));
    }

    @Test
    void searchHotels_ShouldReturnEmptyListWhenNotAllAmenitiesMatch() {
        List<Hotel> result = hotelRepository.searchHotels(
                null,
                null,
                null,
                null,
                List.of("Wi-Fi", "Pool", "Spa")
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void searchHotels_ShouldCombineAllFilters() {
        List<Hotel> result = hotelRepository.searchHotels(
                "Hilton",
                "Hilton",
                "Paris",
                "France",
                List.of("Wi-Fi", "Pool")
        );

        assertEquals(1, result.size());
        assertEquals("Hilton Paris", result.get(0).getName());
    }

    @Test
    void countHotelsByBrand_ShouldReturnCorrectCounts() {
        List<HotelCountProjection> result =
                hotelRepository.countHotelsByBrand();

        Map<String, Long> counts = toCountMap(result);

        assertEquals(2L, counts.get("Hilton"));
        assertEquals(1L, counts.get("Marriott"));
        assertEquals(1L, counts.get("Novotel"));
    }

    @Test
    void countHotelsByCity_ShouldReturnCorrectCounts() {
        List<HotelCountProjection> result =
                hotelRepository.countHotelsByCity();

        Map<String, Long> counts = toCountMap(result);

        assertEquals(2L, counts.get("Paris"));
        assertEquals(1L, counts.get("Lyon"));
        assertEquals(1L, counts.get("London"));
    }

    @Test
    void countHotelsByCountry_ShouldReturnCorrectCounts() {
        List<HotelCountProjection> result =
                hotelRepository.countHotelsByCountry();

        Map<String, Long> counts = toCountMap(result);

        assertEquals(3L, counts.get("France"));
        assertEquals(1L, counts.get("United Kingdom"));
    }

    @Test
    void countHotelsByAmenity_ShouldReturnCorrectCounts() {
        List<HotelCountProjection> result =
                hotelRepository.countHotelsByAmenity();

        Map<String, Long> counts = toCountMap(result);

        assertEquals(4L, counts.get("Wi-Fi"));
        assertEquals(2L, counts.get("Pool"));
        assertEquals(2L, counts.get("Restaurant"));
        assertEquals(2L, counts.get("Parking"));
    }

    private Map<String, Long> toCountMap(
            List<HotelCountProjection> projections
    ) {
        return projections.stream().collect(Collectors.toMap(
                HotelCountProjection::getValue,
                HotelCountProjection::getTotalCount
        ));
    }

    private Hotel createHotel(
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
    ) {
        Address address = new Address();
        address.setHouseNumber(10);
        address.setStreet("Main Street");
        address.setCity(city);
        address.setCountry(country);
        address.setPostCode("12345");

        Contacts contacts = new Contacts();
        contacts.setPhone("+33123456789");
        contacts.setEmail("hotel@example.com");

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setBrand(brand);
        hotel.setAddress(address);
        hotel.setContacts(contacts);
        hotel.setAmenities(amenities);

        return hotel;
    }
}