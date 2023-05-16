package com.sideProject.Nonata.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    double x;
    double y;
    String address;
}
