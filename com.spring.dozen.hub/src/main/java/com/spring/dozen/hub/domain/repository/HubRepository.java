package com.spring.dozen.hub.domain.repository;

import com.spring.dozen.hub.domain.entity.Hub;
import org.springframework.stereotype.Repository;

@Repository
public interface HubRepository {
    Hub save(Hub hub);
}
