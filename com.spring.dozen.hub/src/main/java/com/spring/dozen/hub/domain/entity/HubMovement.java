package com.spring.dozen.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Entity
@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_hub_movement")
public class HubMovement extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "hubMovement_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID hubMovementId;

    @ManyToOne
    @JoinColumn(name = "departure_hub_id", nullable = false)
    private Hub departureHub;

    @ManyToOne
    @JoinColumn(name = "arrival_hub_id", nullable = false)
    private Hub arrivalHub;

    @Column(name = "time", nullable = false)
    private int time; // 초

    @Column(name = "distance", nullable = false)
    private int distance; // 미터

    public static HubMovement create(Hub departureHub,
                                     Hub arrivalHub,
                                     int time,
                                     int distance) {
        HubMovement hubMovement = HubMovement.builder()
                .departureHub(departureHub)
                .arrivalHub(arrivalHub)
                .time(time)
                .distance(distance)
                .build();
        return hubMovement;
    }

    public void update(int time,
                       int distance) {
        this.time = time;
        this.distance = distance;
    }

    public String getFormattedTime() {
        long hours = TimeUnit.SECONDS.toHours(time);
        long minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(hours);
        return (hours > 0 ? hours + "시간 " : "") + minutes + "분";
    }

    public String getFormattedDistance() {
        double kilometers = distance / 1000.0;
        return String.format("%.1fkm", kilometers);
    }
}
