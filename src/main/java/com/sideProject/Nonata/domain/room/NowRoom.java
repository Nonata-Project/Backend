package com.sideProject.Nonata.domain.room;

import com.sideProject.Nonata.domain.Member;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class NowRoom extends Room {

    @OneToOne
    @JoinColumn(name = "OWNER_ID")
    Member owner;
    @OneToMany(mappedBy = "myParticipantNowRoom")
    List<Member> participant;
}
