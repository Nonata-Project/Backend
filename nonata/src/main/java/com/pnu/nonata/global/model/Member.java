package com.pnu.nonata.global.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String kakaoQRUrl;

    private String email;

    private String nickName;

    private String profileImage;

    private double manner;

    private int opportunity;

    private String socialId;

    private String refreshToken;

    @OneToOne(mappedBy = "owner")
    private NowRoom ownedNowRoom;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private NowRoom enteredNowRoom;

    @OneToMany(mappedBy = "member")
    private List<MemberReservationRoom> enteredReservation = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<ReservationRoom> ownedReservationRoom = new ArrayList<>();

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
