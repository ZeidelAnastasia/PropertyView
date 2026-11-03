package com.example.propertyview.repository;

import com.example.propertyview.entity.ArrivalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrivalTimeRepository extends JpaRepository<ArrivalTime, Long> {
}