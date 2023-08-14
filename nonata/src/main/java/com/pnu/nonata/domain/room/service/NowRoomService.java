package com.pnu.nonata.domain.room.service;


import com.pnu.nonata.domain.room.dto.CurrentLocationDto;
import com.pnu.nonata.domain.room.dto.LocationDto;
import com.pnu.nonata.domain.room.repository.NowRoomRepository;
import com.pnu.nonata.global.model.Member;
import com.pnu.nonata.global.model.NowRoom;
import com.pnu.nonata.global.model.Room;
import com.pnu.nonata.global.utils.distance.MapDistanceCalcu;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NowRoomService {

    private final NowRoomRepository nowRoomRepository;

    public List<NowRoom> singleAroundRoom(CurrentLocationDto currentLocationDto) {
        double diffLatitude = MapDistanceCalcu.LatitudeInDifference(500);
        double diffLongitude = MapDistanceCalcu.LongitudeInDifference(currentLocationDto.getLat(), 500);

        double curLatitude = currentLocationDto.getLat();
        double curLongitude = currentLocationDto.getLng();

        return nowRoomRepository.findAllByStartLatitudeBetweenAndStartLongitudeBetween(
                curLatitude - diffLatitude, curLatitude + diffLatitude,
                curLongitude - diffLongitude, curLongitude + diffLongitude
        );
    }

    public List<NowRoom> targetAroundRoom(LocationDto locationDto) {
        double diffLatitude = MapDistanceCalcu.LatitudeInDifference(500);
        double diffStartLongitude = MapDistanceCalcu.LongitudeInDifference(locationDto.getStartLat(), 500);
        double diffDestLongitude = MapDistanceCalcu.LongitudeInDifference(locationDto.getDestLat(), 500);

        return nowRoomRepository.findAllByStartLatitudeBetweenAndStartLongitudeBetweenAndDestLatitudeBetweenAndDestLongitude(
                locationDto.getStartLat() - diffLatitude, locationDto.getStartLat() + diffLatitude,
                locationDto.getStartLng() - diffStartLongitude, locationDto.getStartLng() + diffStartLongitude,
                locationDto.getDestLat() - diffLatitude, locationDto.getDestLat() + diffLatitude,
                locationDto.getDestLng() - diffDestLongitude, locationDto.getDestLng() + diffDestLongitude
        );
    }

    public NowRoom findNowRoom(long room_id) {
        return nowRoomRepository.findById(room_id).orElse(null);
    }

    public boolean enterRoom(Member member, long room_id) throws NullPointerException {

        NowRoom selectedRoom = findNowRoom(room_id);

        if (selectedRoom == null)
            throw new NullPointerException("SELECTED ROOM IS DOESN'T EXIST");


        if (selectedRoom.getParticipants().size() == selectedRoom.getLimitMemberSize())
            return false;

        selectedRoom.getParticipants().add(member);

        return true;
    }

    public void makeRoom(Member member, NowRoom room) {

        room.setOwner(member);
        room.getParticipants().add(member);

        nowRoomRepository.save(room);
    }
}