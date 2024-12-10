package com.spring.dozen.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "p_hub")
public class Hub {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID hubId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "central_hub_id", columnDefinition = "BINARY(16)")
    private UUID centralHubId;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "location_x", columnDefinition = "DECIMAL(10,8)", nullable = false)
    private Double locationX;

    @Column(name = "location_y", columnDefinition = "DECIMAL(10,8)", nullable = false)
    private Double locationY;

    @Column(name = "is_deleted")
    private boolean isDeleted;

}
