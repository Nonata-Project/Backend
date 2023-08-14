package com.pnu.nonata.global.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Location {
    double latitude;

    double longitude;

    String address;

}
