package com.pnu.nonata.global.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ROOM_ID")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="latitude", column = @Column(name="DEST_LATITUDE")),
            @AttributeOverride(name="longitude", column = @Column(name="DEST_LONGITUDE")),
            @AttributeOverride(name="address", column = @Column(name="DEST_ADDRESSS"))
    })
    private Location dest;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="latitude", column = @Column(name="START_LATITUDE")),
            @AttributeOverride(name="longitude", column = @Column(name="START_LONGITUDE")),
            @AttributeOverride(name="address", column = @Column(name="START_ADDRESSS"))
    })
    private Location start;

    private int cost;

    private ZonedDateTime limitEnterTime;

    private int limitMemberSize;
}
