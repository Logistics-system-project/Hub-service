package com.spring.dozen.hub.domain.repository;

import com.spring.dozen.hub.domain.entity.HubMovement;
import org.springframework.stereotype.Repository;


@Repository
public interface HubMovementRepository {
    HubMovement save(HubMovement hubMovement);
}
