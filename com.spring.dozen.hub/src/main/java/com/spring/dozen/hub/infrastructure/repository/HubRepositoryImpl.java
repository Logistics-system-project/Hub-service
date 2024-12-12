package com.spring.dozen.hub.infrastructure.repository;

import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.repository.HubRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface HubRepositoryImpl extends JpaRepository<Hub, UUID>, HubRepository {

    @Query(value = "SELECT * FROM hub " +
            "WHERE (:keyword IS NULL OR :keyword = '' OR address LIKE %:keyword% OR userId LIKE %:keyword%)",
            nativeQuery = true)
    Page<Hub> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
