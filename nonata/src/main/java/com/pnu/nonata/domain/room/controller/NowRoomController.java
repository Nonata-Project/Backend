package com.pnu.nonata.domain.room.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnu.nonata.domain.room.dto.CurrentLocationDto;
import com.pnu.nonata.domain.room.dto.LocationDto;
import com.pnu.nonata.domain.room.dto.NowRoomDto;
import com.pnu.nonata.domain.room.service.NowRoomService;
import com.pnu.nonata.domain.room.service.UserService;
import com.pnu.nonata.global.model.Member;
import com.pnu.nonata.global.model.NowRoom;
import com.pnu.nonata.global.model.Room;
import com.pnu.nonata.global.utils.distance.MapDistanceCalcu;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/room/now")
@Slf4j
@RequiredArgsConstructor
public class NowRoomController {

    private final NowRoomService nowRoomService;
    private final UserService userService;

    /**
     * 특정 위치 값이 들어오면 주변 500m 반경 방 정보를 보여준다.
     * 도착지는 상관없이 보여준다.
     *
     * > 현재 위치(lat lng)를 기반으로 현재방에서 일치하는 방 찾기
     */
    @GetMapping("/around")
    public void findAroundRoom(@RequestBody CurrentLocationDto currentLocationDto, HttpServletResponse httpResponse) throws IOException {

        List<NowRoom> rooms= nowRoomService.singleAroundRoom(currentLocationDto);

        double range = 500;

        double curLat = currentLocationDto.getLat();
        double curLng = currentLocationDto.getLng();

        List<NowRoomDto> result = new ArrayList<>();

        for (NowRoom room : rooms) {

            double targetLat = room.getStart().getLatitude();
            double targetLng = room.getStart().getLongitude();

            if (MapDistanceCalcu.distance(curLat,targetLat,curLng,targetLng,"meter") > range)
                continue;

            if(room.getLimitEnterTime().isBefore(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))))
                continue;

            if(room.getParticipants().size()==room.getLimitMemberSize())
                continue;

            result.add(NowRoomDto.builder()
                        .room_id(room.getId())
                        .start(room.getStart())
                        .dest(room.getDest())
                        .cost(room.getCost())
                        .member_size(room.getParticipants().size())
                    .build());
        }

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }


    /**
     * 특정 위치 값이 들어오면 주변 500m 반경 방 정보를 보여준다.
     * 출발지와 도착지 반경 500m 일치하는 방을 보여준다.
     */
    @GetMapping("/find")
    public void findTargetRoom(@RequestBody LocationDto locationDto, HttpServletResponse httpResponse) throws IOException {
        List<NowRoom> rooms= nowRoomService.targetAroundRoom(locationDto);

        double range = 500;

        List<NowRoomDto> result = new ArrayList<>();


        for (NowRoom room : rooms) {

            if (MapDistanceCalcu.distance(locationDto.getStartLat(), room.getStart().getLatitude(),
                    locationDto.getStartLng(), room.getStart().getLongitude(),"meter") > range)
                continue;

            if (MapDistanceCalcu.distance(locationDto.getDestLat(), room.getDest().getLatitude(),
                    locationDto.getDestLng(), room.getDest().getLongitude(),"meter") > range)
                continue;

            if(room.getLimitEnterTime().isBefore(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))))
                continue;

            if(room.getParticipants().size()==room.getLimitMemberSize())
                continue;

            result.add(NowRoomDto.builder()
                    .room_id(room.getId())
                    .start(room.getStart())
                    .dest(room.getDest())
                    .cost(room.getCost())
                    .member_size(room.getParticipants().size())
                    .build());
        }

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }

    /**
     * 선택한 방에 입장
     */
    @PatchMapping("/enter")
    public void enterTargetRoom(@RequestParam("id") Long room_id ,HttpServletResponse httpResponse,
                               @AuthenticationPrincipal UserDetails principal) throws IOException {

        // 유저 정보 가져오기
        Member member = userService.findUser(principal.getUsername());

        if(member==null)
            throw new NullPointerException("MEMBER NOT EXIST");

        // 방 입장
        Map<String,Object> response = new HashMap<>();

        if(nowRoomService.enterRoom(member, room_id))
            response.put("id", room_id);
        else
            response.put("id", String.valueOf(-1));

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }

    /**
     * 방 만들기
     */
    @PostMapping("/create")
    public void createNowRoom(@RequestBody NowRoomDto nowRoomDto,
                              HttpServletResponse httpResponse, @AuthenticationPrincipal UserDetails principal) {


        // todo 시간 관련 연산 처리 하기
        /**
         * 1. 시간은 클라에서 어떻게 받을 건지
         * 2. zonedDateTime 연산
         */
        ZonedDateTime currentTimeOfKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));


    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Map<String,Object>> nullPointerExceptionHandler(Exception e) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

        Map<String,Object> map = new HashMap<>();
        map.put("msg", e.getMessage());
        map.put("status", HttpStatus.ACCEPTED);
        map.put("timestamp", String.valueOf(sdf.format(timestamp)));

        return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
    }
}
