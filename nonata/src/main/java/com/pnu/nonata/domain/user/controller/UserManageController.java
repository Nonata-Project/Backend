package com.pnu.nonata.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnu.nonata.domain.user.dto.UserDto;
import com.pnu.nonata.domain.user.repository.UserMemberRepository;
import com.pnu.nonata.domain.user.service.UserService;
import com.pnu.nonata.global.model.Member;
import jakarta.persistence.NoResultException;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/user")
@Slf4j
@RequiredArgsConstructor
public class UserManageController {

    private final UserService userService;
    private final UserMemberRepository userMemberRepository;

    // 수정 조회 로그아웃
    @GetMapping("/test")
    public String test(){
        return "OK";
    }


    @PostMapping("/update")
    public void updateUser(@RequestBody UserDto body, HttpServletResponse httpResponse,
                           @AuthenticationPrincipal UserDetails principal)
    {
        Member member = userMemberRepository.findBySocialId(principal.getUsername()).orElse(null);

        if(member==null)
            throw new NullPointerException("MEMBER NOT EXIST");

        if(body==null)
            throw new NullPointerException("NULL DATA");

        if(body.getProfile_image()!=null)
            member.setProfileImage(body.getProfile_image());

        if(body.getNick_name()!=null)
            member.setNickName(body.getNick_name());

        if(body.getKakao_QR_url()!=null)
            member.setKakaoQRUrl(body.getKakao_QR_url());

        if(!userService.saveUser(member))
            throw new NoResultException("FAIL TO UPDATE USER");

        httpResponse.setStatus(HttpServletResponse.SC_OK);
    }

    @GetMapping("/info")
    public void searchUser(HttpServletResponse httpResponse,@AuthenticationPrincipal UserDetails principal)
            throws IOException
    {

        Member member = userMemberRepository.findBySocialId(principal.getUsername()).orElse(null);

        if(member==null)
            throw new NullPointerException("MEMBER NOT EXIST");

        Map<String,Object> map= new HashMap<>();
        map.put("nick_name", member.getNickName());
        map.put("opportunity", member.getOpportunity());
        map.put("manner", member.getManner());
        map.put("kakao_QR_url", member.getKakaoQRUrl());

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(map));
    }

    @DeleteMapping("/resignation")
    public void logoutUser( HttpServletResponse httpResponse, @AuthenticationPrincipal UserDetails principal)
    {
        Member member = userMemberRepository.findBySocialId(principal.getUsername()).orElse(null);

        if(member==null)
            throw new NullPointerException("MEMBER NOT EXIST");

        userMemberRepository.delete(member);

        httpResponse.setStatus(HttpServletResponse.SC_OK);


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

    @ExceptionHandler(NoResultException.class)
    protected ResponseEntity<Map<String,Object>> dbHandler(Exception e) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

        Map<String,Object> map = new HashMap<>();
        map.put("msg", e.getMessage());
        map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        map.put("timestamp", String.valueOf(sdf.format(timestamp)));

        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
