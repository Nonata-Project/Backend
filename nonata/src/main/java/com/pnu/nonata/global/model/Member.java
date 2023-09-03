package com.pnu.nonata.global.model;

import com.pnu.nonata.global.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String kakaoQRUrl;

    private String nickName;

    private String profileImage;

    private double manner;

    private int opportunity;

    private String socialId;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

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
