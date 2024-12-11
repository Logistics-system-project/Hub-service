package com.spring.dozen.hub.infrastructure.repository;

import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.repository.HubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRepositoryImpl extends JpaRepository<Hub, UUID>, HubRepository {
}
