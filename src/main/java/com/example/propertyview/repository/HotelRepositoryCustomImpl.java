package com.example.propertyview.repository;

import com.example.propertyview.entity.Hotel;
import com.example.propertyview.query.HotelQueryBuilder;
import com.example.propertyview.repository.projection.HotelCountProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HotelRepositoryCustomImpl implements HotelRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Hotel> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hotel> query = cb.createQuery(Hotel.class);
        Root<Hotel> root = query.from(Hotel.class);

        query.select(root);

        query.where(
                HotelQueryBuilder.buildHotelFilterPredicates(
                        cb, root, query,
                        name, brand, city, country, amenities
                ).toArray(new jakarta.persistence.criteria.Predicate[0])
        );

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<HotelCountProjection> countHotelsByBrand() {
        return countByField("brand");
    }

    @Override
    public List<HotelCountProjection> countHotelsByCity() {
        return countByField("address.city");
    }

    @Override
    public List<HotelCountProjection> countHotelsByCountry() {
        return countByField("address.country");
    }

    @Override
    public List<HotelCountProjection> countHotelsByAmenity() {
        return countByField("amenities");
    }

    private List<HotelCountProjection> countByField(String field) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<Hotel> root = query.from(Hotel.class);

        Expression<String> value;

        if ("address.city".equals(field)) {
            value = root.get("address").get("city");
        } else if ("address.country".equals(field)) {
            value = root.get("address").get("country");
        } else if ("amenities".equals(field)) {
            value = root.join("amenities", JoinType.INNER);
        } else {
            value = root.get(field);
        }

        Expression<Long> count = cb.count(root);

        if ("amenities".equals(field)) {
            count = cb.countDistinct(root);
        }

        query.multiselect(
                value.alias("value"),
                count.alias("totalCount")
        );

        query.groupBy(value);

        if ("brand".equals(field)) {
            query.where(cb.isNotNull(value));
        }

        List<HotelCountProjection> result = entityManager.createQuery(query)
                .getResultList()
                .stream()
                .<HotelCountProjection>map(tuple -> new HotelCountProjection() {
                    @Override
                    public String getValue() {
                        return tuple.get("value", String.class);
                    }

                    @Override
                    public Long getTotalCount() {
                        return tuple.get("totalCount", Long.class);
                    }
                })
                .toList();

        return result;
    }
}