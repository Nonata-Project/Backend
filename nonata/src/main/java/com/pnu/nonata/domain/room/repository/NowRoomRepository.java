package com.pnu.nonata.domain.room.repository;


import com.pnu.nonata.global.model.NowRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NowRoomRepository extends JpaRepository<NowRoom, Long> {

    List<NowRoom> findAllByStartLatitudeBetweenAndStartLongitudeBetween(
            double lowLatitude, double highLatitude,
            double lowLongitude, double highLongitude
    );


    List<NowRoom> findAllByStartLatitudeBetweenAndStartLongitudeBetweenAndDestLatitudeBetweenAndDestLongitude(
            double lowStartLatitude, double highStartLatitude,
            double lowStartLongitude, double highStartLongitude,
            double lowDestLatitude, double highDestLatitude,
            double lowDestLongitude, double highDestLongitude
    );

}
