package com.pnu.nonata.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    double latitude;

    double longitude;

    String address;

}
