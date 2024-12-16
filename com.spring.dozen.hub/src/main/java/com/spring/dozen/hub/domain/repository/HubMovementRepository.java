package com.spring.dozen.hub.domain.repository;

import com.spring.dozen.hub.domain.entity.HubMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface HubMovementRepository {
    HubMovement save(HubMovement hubMovement);
    Page<HubMovement> findByKeyword(String keyword, Pageable pageable);
    Optional<HubMovement> findByHubMovementIdAndIsDeletedFalse (UUID hubMovementId);
}
