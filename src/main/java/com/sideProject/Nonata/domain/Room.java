package com.sideProject.Nonata.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Location start;
    Location dest;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    Member owner;

    int cost;
    LocalDateTime limitTime;
    int limitMember;

    @OneToMany(mappedBy = "room")
    List<RoomIntersection> participants;

    @Enumerated(EnumType.STRING)
    RoomType type;
    LocalDateTime rideTime;

}
