package com.pnu.nonata.global.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pnu.nonata.global.jwt.repository.JwtMemberRepository;
import com.pnu.nonata.global.jwt.service.JwtService;
import com.pnu.nonata.global.model.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtMemberRepository userRepository;

    @Value("${jwt.access.header}")
    private String accessHeader;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader(accessHeader);

        if(accessToken==null){
            filterChain.doFilter(request,response);
            return;
        }

        checkAccessTokenAndAuthentication(request);

        if(SecurityContextHolder.getContext().getAuthentication()!=null&&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            filterChain.doFilter(request,response);
            return;
        }

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        checkRefreshTokenAndReIssueAccessToken(response, refreshToken);

        if(response.getStatus()==HttpServletResponse.SC_FORBIDDEN)
            throw new JWTVerificationException("JWT TOKEN EXPIRED OR INVALID");


    }


    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(user);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getSocialId()),
                            reIssuedRefreshToken);
                });
    }

    private String reIssueRefreshToken(Member user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken(user.getSocialId());
        jwtService.updateRefreshToken(user.getSocialId(),reIssuedRefreshToken);

        return reIssuedRefreshToken;
    }


    public void checkAccessTokenAndAuthentication(HttpServletRequest request) {
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractId(accessToken)
                        .ifPresent(socialId -> userRepository.findBySocialId(socialId)
                                .ifPresent(this::saveAuthentication)));

    }


    public void saveAuthentication(Member myUser) {
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getSocialId())
                .password(UUID.randomUUID().toString())
                .roles(myUser.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
