package com.example.auth.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//    @Bean
//    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Общедоступные маршруты
                        .requestMatchers(
                                "/login",
                                "/registration",
                                "/favicon.ico",
                                "/oops",
                                "/css/**",
                                "/js/**",
                                "/email-page",
                                "/sendEmail"
                        ).permitAll()

                        // Админ-панель (зарезервировано)
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Маршруты доступны USER и ADMIN
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/afterLogin", true)
                        .failureUrl("/login?error")
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                )

                .csrf(AbstractHttpConfigurer::disable); // CSRF в целях для разработки отключается

        return http.build();
    }
}
