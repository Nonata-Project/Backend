package com.sideProject.Nonata.domain;

import jakarta.persistence.*;
import lombok.Getter;

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
    @OneToMany(mappedBy = "participant")
    List<RoomIntersection> myParticipantRoom;
    @OneToMany(mappedBy = "owner")
    List<Room> myRoom;
}
