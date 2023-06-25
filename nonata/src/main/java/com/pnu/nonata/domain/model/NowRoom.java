package com.pnu.nonata.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.List;

@Entity
public class NowRoom extends Room{

    @OneToOne
    @JoinColumn(name="MEMBER_ID")
    private Member owner;

    @OneToMany(mappedBy = "enteredNowRoom")
    private List<Member> participants = new ArrayList<Member>();;
}
