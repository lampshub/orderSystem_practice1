package com.beyond23.orderSystem.ordering.repository;


import com.beyond23.orderSystem.ordering.domain.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderingRepository extends JpaRepository<Ordering, Long> {
}
