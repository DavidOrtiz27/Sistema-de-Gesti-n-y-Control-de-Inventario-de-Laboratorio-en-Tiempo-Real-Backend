package com.santuarylab.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //sirve para marcar los filtros de seguridad que se usan en la app
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated());

        return http.build();
    }
}
