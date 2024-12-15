package com.spring.dozen.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

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
    private int time;

    @Column(name = "distance", nullable = false)
    private int distance;

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

    public void update(Hub departureHub,
                       Hub arrivalHub,
                       int time,
                       int distance) {
        this.departureHub = departureHub;
        this.arrivalHub = arrivalHub;
        this.time = time;
        this.distance = distance;
    }

    public String getFormattedTime() {
        int hours = time / 60;
        int minutes = time % 60;
        return (hours > 0 ? hours + "시간 " : "") + minutes + "분";
    }
}
