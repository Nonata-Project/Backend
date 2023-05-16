package com.sideProject.Nonata.domain.room;

import com.sideProject.Nonata.domain.Location;
import com.sideProject.Nonata.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn
public abstract class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    Location dest;

    int cost;
    LocalDateTime limitTime;
    int limitMember;

}
