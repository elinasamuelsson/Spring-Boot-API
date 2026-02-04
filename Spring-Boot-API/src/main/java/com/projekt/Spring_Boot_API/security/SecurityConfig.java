package com.projekt.Spring_Boot_API.security;

import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import com.projekt.Spring_Boot_API.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            IUserRepository userRepository,
            JwtService jwtService
    ) {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/api/users/register").permitAll()
                            .requestMatchers("/api/users/login").permitAll()
                            .requestMatchers("/api/users/get-all").permitAll() //TODO: remove before finalizing
                            .anyRequest().authenticated();
                })
                .addFilterBefore(
                        new JwtAuthenticationFilter(userRepository, jwtService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}