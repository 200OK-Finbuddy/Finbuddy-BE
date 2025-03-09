package com.http200ok.finbuddy.config;

import com.http200ok.finbuddy.member.repository.MemberRepository;
import com.http200ok.finbuddy.security.CustomUserDetailsService;
import com.http200ok.finbuddy.security.JwtAuthenticationFilter;
import com.http200ok.finbuddy.security.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(request -> {
//                    CorsConfiguration config = new CorsConfiguration();
//                    config.setAllowedOrigins(List.of("http://localhost:5173")); // ✅ 프론트엔드 주소
//                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
//                    config.setAllowCredentials(true); // ✅ 브라우저에서 쿠키 허용 필수
//                    config.setAllowedHeaders(List.of("*"));
//                    config.setExposedHeaders(List.of("Set-Cookie")); // ✅ 클라이언트가 여러 개의 Set-Cookie를 읽을 수 있도록 허용
//                    return config;
//                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
//                        .anyRequest().authenticated()
//                        .anyRequest().permitAll()
                ).addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService, memberRepository),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}