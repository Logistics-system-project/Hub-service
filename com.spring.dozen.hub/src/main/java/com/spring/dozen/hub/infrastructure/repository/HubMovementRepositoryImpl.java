package com.spring.dozen.hub.infrastructure.repository;

import com.spring.dozen.hub.domain.entity.HubMovement;
import com.spring.dozen.hub.domain.repository.HubMovementRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubMovementRepositoryImpl extends JpaRepository<HubMovement, UUID>, HubMovementRepository {


}
