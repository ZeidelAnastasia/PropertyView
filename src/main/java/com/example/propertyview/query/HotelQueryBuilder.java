package com.example.propertyview.query;

import com.example.propertyview.entity.Hotel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public final class HotelQueryBuilder {

    private HotelQueryBuilder() {
    }

    public static List<Predicate> buildHotelFilterPredicates(
            CriteriaBuilder cb,
            Root<Hotel> root,
            CriteriaQuery<?> query,
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("name")),
                            "%" + name.toLowerCase() + "%"
                    )
            );
        }

        if (brand != null && !brand.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("brand")),
                            "%" + brand.toLowerCase() + "%"
                    )
            );
        }

        if (city != null && !city.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("address").get("city")),
                            "%" + city.toLowerCase() + "%"
                    )
            );
        }

        if (country != null && !country.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("address").get("country")),
                            "%" + country.toLowerCase() + "%"
                    )
            );
        }

        if (amenities != null && !amenities.isEmpty()) {
            Join<Hotel, String> amenitiesJoin =
                    root.join("amenities", JoinType.INNER);

            predicates.add(
                    amenitiesJoin.in(amenities)
            );

            query.distinct(true);
        }

        return predicates;
    }
}