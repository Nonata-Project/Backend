package com.pnu.nonata.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class ReservationRoom extends Room{

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member owner;

    @OneToMany(mappedBy = "room")
    private List<MemberReservationRoom> participants;

    private LocalDateTime rideTime;
}
