package com.spring.dozen.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_hub")
public class Hub extends BaseEntity {
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

    @Column(name = "location_x", columnDefinition = "DECIMAL(12,8)", nullable = false)
    private Double locationX;

    @Column(name = "location_y", columnDefinition = "DECIMAL(12,8)", nullable = false)
    private Double locationY;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public static Hub create(Long userId,
                             UUID centralHubId,
                             String address,
                             Double locationX,
                             Double locationY) {
        Hub hub = Hub.builder()
                .userId(userId)
                .centralHubId(centralHubId)
                .address(address)
                .locationX(locationX)
                .locationY(locationY)
                .build();
        return hub;
    }
}
