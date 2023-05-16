package com.sideProject.Nonata.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class RoomIntersection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "PARTICIPANT_ID")
    Member participant;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    Room room;
}
