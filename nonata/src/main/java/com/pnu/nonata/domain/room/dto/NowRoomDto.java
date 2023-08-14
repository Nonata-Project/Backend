package com.pnu.nonata.domain.room.dto;

import com.pnu.nonata.global.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class NowRoomDto {

    Long room_id;

    Location start;

    Location dest;

    int cost;

    int member_size;

}
