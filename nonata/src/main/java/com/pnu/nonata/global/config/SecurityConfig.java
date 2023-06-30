package com.pnu.nonata.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnu.nonata.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.pnu.nonata.global.jwt.service.JwtService;
import com.pnu.nonata.global.model.repository.MemberRepository;
import com.pnu.nonata.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.pnu.nonata.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.pnu.nonata.global.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        (formLogin)->formLogin.disable()
                )
                .httpBasic(
                        (httpBasic)->httpBasic.disable()
                )
                .csrf(
                        (csrf)->csrf.disable()
                )
                .headers(
                        (headers)->
                                headers
                                        .frameOptions((frameOptions)->frameOptions.disable())
                )
                .sessionManagement(
                        (sessionManegement)->
                                sessionManegement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //== URL별 권한 관리 옵션 ==//
                .authorizeRequests(
                        (authorizeRequests)->
                                authorizeRequests.requestMatchers("","/","/css/**","/images/**","/js/**","/favicon.ico").permitAll()
                                        .anyRequest().authenticated()

                )
                //== 소셜 로그인 설정 ==//
                .oauth2Login(
                        (oauth2Login)->
                                oauth2Login
                                        .successHandler(oAuth2LoginSuccessHandler)
                                        .failureHandler(oAuth2LoginFailureHandler)
                                        .userInfoEndpoint(
                                                (userInfoEndpoint)->userInfoEndpoint.userService(customOAuth2UserService)
                                        )
                );


        http.addFilterAfter(jwtAuthenticationProcessingFilter(), LogoutFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
        return jwtAuthenticationFilter;
    }
}
