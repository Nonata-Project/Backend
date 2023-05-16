package com.sideProject.Nonata.domain;

import com.sideProject.Nonata.domain.room.NowRoom;
import com.sideProject.Nonata.domain.room.NowRoomIntersection;
import com.sideProject.Nonata.domain.room.ReservationRoom;
import com.sideProject.Nonata.domain.room.ReservationRoomIntersection;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String qr;
    String email;
    String nickName;
    String profileImage;
    double manner;
    int opportunity;
    @OneToOne(mappedBy = "owner")
    NowRoom myNowRoom;
    @ManyToOne
    @JoinColumn(name = "NOW_ROOM_ID")
    NowRoom myParticipantNowRoom;
    @OneToMany(mappedBy = "owner")
    List<ReservationRoom> myReservationRoom = new ArrayList<>();
    @OneToMany(mappedBy = "participant")
    List<ReservationRoomIntersection> myParticipantReservationRoom = new ArrayList<>();
}
