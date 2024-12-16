package com.spring.dozen.hub.infrastructure.repository;

import com.spring.dozen.hub.domain.entity.HubMovement;
import com.spring.dozen.hub.domain.repository.HubMovementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface HubMovementRepositoryImpl extends JpaRepository<HubMovement, UUID>, HubMovementRepository {
    @Query(value = "SELECT hm FROM HubMovement hm " +
            "WHERE hm.isDeleted = false " +
            "AND (:keyword IS NULL OR :keyword = '' OR hm.departureHub.address LIKE %:keyword% OR hm.arrivalHub.address LIKE %:keyword%)")
    Page<HubMovement> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
