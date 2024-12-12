package com.spring.dozen.hub.domain.repository;

import com.spring.dozen.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HubRepository {
    Hub save(Hub hub);

    Page<Hub> findAll(Pageable pageable);

    Page<Hub> findByKeyword( String keyword, Pageable pageable);
}
