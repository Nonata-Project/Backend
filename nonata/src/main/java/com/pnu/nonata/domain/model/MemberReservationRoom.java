package com.pnu.nonata.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity(name="MEMBER_RESERVATION")
@Getter
public class MemberReservationRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private ReservationRoom room;

}
