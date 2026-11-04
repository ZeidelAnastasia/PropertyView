
package com.example.propertyview.mapper;

import com.example.propertyview.dto.request.AddressRequest;
import com.example.propertyview.dto.request.ArrivalTimeRequest;
import com.example.propertyview.dto.request.ContactsRequest;
import com.example.propertyview.dto.request.CreateHotelRequest;

import com.example.propertyview.dto.response.AddressResponse;
import com.example.propertyview.dto.response.ArrivalTimeResponse;
import com.example.propertyview.dto.response.ContactsResponse;
import com.example.propertyview.dto.response.HotelDetailsResponse;
import com.example.propertyview.dto.response.HotelSummaryResponse;

import com.example.propertyview.entity.Address;
import com.example.propertyview.entity.ArrivalTime;
import com.example.propertyview.entity.Contacts;
import com.example.propertyview.entity.Hotel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntity(CreateHotelRequest request);

    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressRequest request);

    @Mapping(target = "id", ignore = true)
    Contacts toEntity(ContactsRequest request);

    @Mapping(target = "id", ignore = true)
    ArrivalTime toEntity(ArrivalTimeRequest request);

    @Mapping(
            target = "address",
            expression = "java(formatAddress(hotel.getAddress()))"
    )
    @Mapping(target = "phone", source = "contacts.phone")
    HotelSummaryResponse toSummaryResponse(Hotel hotel);

    HotelDetailsResponse toDetailsResponse(Hotel hotel);

    AddressResponse toAddressResponse(Address address);

    ContactsResponse toContactsResponse(Contacts contacts);

    ArrivalTimeResponse toArrivalTimeResponse(ArrivalTime arrivalTime);

    default String formatAddress(Address address) {
        if (address == null) {
            return null;
        }

        return address.getHouseNumber() + " "
                + address.getStreet() + ", "
                + address.getCity() + ", "
                + address.getPostCode() + ", "
                + address.getCountry();
    }
}
