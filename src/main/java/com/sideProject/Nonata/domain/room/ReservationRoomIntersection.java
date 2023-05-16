package com.sideProject.Nonata.domain.room;

import com.sideProject.Nonata.domain.Member;
import com.sideProject.Nonata.domain.room.ReservationRoom;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ReservationRoomIntersection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "PARTICIPANT_ID")
    Member participant;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    ReservationRoom room;
}
