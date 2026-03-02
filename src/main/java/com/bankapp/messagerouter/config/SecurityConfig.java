package com.bankapp.messagerouter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // désactive CSRF pour API REST
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // protège toutes les routes
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt() // validation JWT Keycloak
                );

        return http.build();
    }
}
