package com.spring.dozen.hub.domain.repository;

import com.spring.dozen.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubRepository {
    Hub save(Hub hub);

    Page<Hub> findByKeyword( String keyword, Pageable pageable);
    Optional<Hub> findByHubIdAndIsDeletedFalse (UUID hubId);
    Optional<Hub> findByHubName (String hubName);
    boolean existsByHubName (String hubName);

    boolean existsByHubIdAndIsDeletedFalse(UUID hubId);

}
