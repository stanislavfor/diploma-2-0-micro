package com.example.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/images/sendEmail",
                                "/api/images/email-page",
                                "/login", "/registration", "/oops", "/index.html",
//                                "/", "/images/add"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
//                .oauth2Login(withDefaults()) // Keycloak как OAuth2 логин-провайдер
                .logout(logout -> logout.logoutSuccessUrl("/login"))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
