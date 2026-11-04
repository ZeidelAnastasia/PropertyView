package com.example.propertyview.query;

import com.example.propertyview.entity.Hotel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
                            "%" + name.toLowerCase(Locale.ROOT) + "%"
                    )
            );
        }

        if (brand != null && !brand.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("brand")),
                            "%" + brand.toLowerCase(Locale.ROOT) + "%"
                    )
            );
        }

        if (city != null && !city.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("address").get("city")),
                            "%" + city.toLowerCase(Locale.ROOT) + "%"
                    )
            );
        }

        if (country != null && !country.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("address").get("country")),
                            "%" + country.toLowerCase(Locale.ROOT) + "%"
                    )
            );
        }

        if (amenities != null && !amenities.isEmpty()) {
            Set<String> requestedAmenities = new HashSet<>(amenities);

            Subquery<Long> amenitiesSubquery = query.subquery(Long.class);
            Root<Hotel> amenitiesRoot = amenitiesSubquery.from(Hotel.class);

            Join<Hotel, String> amenitiesJoin =
                    amenitiesRoot.join("amenities", JoinType.INNER);

            amenitiesSubquery
                    .select(cb.countDistinct(amenitiesJoin))
                    .where(
                            cb.equal(
                                    amenitiesRoot.get("id"),
                                    root.get("id")
                            ),
                            amenitiesJoin.in(requestedAmenities)
                    );

            predicates.add(
                    cb.equal(
                            amenitiesSubquery,
                            (long) requestedAmenities.size()
                    )
            );
        }

        return predicates;
    }
}