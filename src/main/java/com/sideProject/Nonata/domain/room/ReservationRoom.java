package com.sideProject.Nonata.domain.room;

import com.sideProject.Nonata.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ReservationRoom extends Room {

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    Member owner;

    @OneToMany(mappedBy = "room")
    List<ReservationRoomIntersection> participants;

    LocalDateTime rideTime;
}
