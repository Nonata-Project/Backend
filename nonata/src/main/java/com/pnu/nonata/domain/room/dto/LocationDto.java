package com.pnu.nonata.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class LocationDto {
    private Double startLat;
    private Double startLng;
    private Double destLat;
    private Double destLng;
}
