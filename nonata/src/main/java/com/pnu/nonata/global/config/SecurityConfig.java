package com.pnu.nonata.global.config;


import com.pnu.nonata.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.pnu.nonata.global.jwt.filter.JwtExceptionFilter;
import com.pnu.nonata.global.jwt.repository.JwtMemberRepository;
import com.pnu.nonata.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final JwtMemberRepository memberRepository;

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
                                authorizeRequests.requestMatchers("/api/v1/auth/**").hasRole("USER")
                                        .requestMatchers("/api/v1/**","/css/**","/images/**","/js/**","/favicon.ico").permitAll()


                );

        http.addFilterAfter(jwtExceptionFilter(), LogoutFilter.class);
        http.addFilterAfter(jwtAuthenticationProcessingFilter(), LogoutFilter.class);


        return http.build();
    }


    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
        return jwtAuthenticationFilter;
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter();
        return jwtExceptionFilter;
    }
}
