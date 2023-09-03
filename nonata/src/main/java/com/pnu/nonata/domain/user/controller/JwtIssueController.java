package com.pnu.nonata.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnu.nonata.domain.user.service.UserService;
import com.pnu.nonata.global.jwt.service.JwtService;
import com.pnu.nonata.global.model.Member;
import com.pnu.nonata.global.model.enums.Role;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
@RequiredArgsConstructor
public class JwtIssueController {

    private final UserService userService;

    private final JwtService jwtService;

    @PostMapping("/oauth/2.0/token")
    public void getKakaoToken(@RequestBody @Nullable HashMap<String, Object> body, HttpServletResponse httpResponse) throws HttpClientErrorException {

        String accessToken= null;

        if(body!=null)
            accessToken=(String)body.get("access_token");

        if(accessToken==null||accessToken.equals("")){
            throw new NullPointerException("ACCESS TOKEN IS EMPTY");
        }

        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
                kakaoProfileRequest, String.class);


        Map<String, Object> userInfo = null;
        try {
            userInfo = new ObjectMapper().readValue(response.getBody(), Map.class) ;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Map<String, Object> account = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (account == null || profile == null) {
            throw new NullPointerException("NO EXIST USER DATA");
        }

        Map<String,String> token = new TreeMap<>();
        token.put("access_token",jwtService.createAccessToken(userInfo.get("id").toString(), Role.USER.name()));
        token.put("refresh_token",jwtService.createRefreshToken(userInfo.get("id").toString()));

        if(!userService.isUserExist(userInfo.get("id").toString())){
            //todo 새로운 회원 등록
            Member member = Member.builder()
                    .nickName(profile.get("nickname").toString())
                    .socialId(userInfo.get("id").toString())
                    .manner(0.0d)
                    .role(Role.USER)
                    .opportunity(2)
                    .refreshToken(token.get("refresh_token"))
                    .build();



            if(!userService.saveUser(member))
                throw new NoResultException("FAIL TO SAVE USER IN DB");
        }


        jwtService.sendAccessAndRefreshToken(httpResponse,token.get("access_token"),
        token.get("refresh_token"));

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

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Map<String,Object>> httpClientErrorExceptionHandler(HttpClientErrorException e) throws JsonProcessingException {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

        Map<String,Object> map = new HashMap<>();
        Map<String,Object> exceptionMsg = new ObjectMapper().readValue(e.getResponseBodyAsString(), Map.class);

        map.put("msg", exceptionMsg.get("msg").toString());
        map.put("status", e.getStatusCode().value());
        map.put("timestamp", String.valueOf(sdf.format(timestamp)));


        return new ResponseEntity<>(map, e.getStatusCode());
    }

    @ExceptionHandler({JsonMappingException.class, JsonProcessingException.class})
    protected ResponseEntity<Map<String,Object>> jsonHandler(Exception e) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

        Map<String,Object> map = new HashMap<>();
        map.put("msg", e.getMessage());
        map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        map.put("timestamp", String.valueOf(sdf.format(timestamp)));

        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
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
