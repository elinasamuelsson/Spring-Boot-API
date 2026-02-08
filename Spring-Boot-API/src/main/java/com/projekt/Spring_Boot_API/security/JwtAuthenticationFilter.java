package com.projekt.Spring_Boot_API.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final IUserRepository userRepository;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || header.isBlank() || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring("Bearer ".length());

        UUID userId;
        try {
            userId = jwtService.validateToken(token);
        } catch (JWTVerificationException e) {
            response.setStatus(401);
            return;
        }

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isEmpty()) {
            response.setStatus(401);
            return;
        }

        User user = optionalUser.get();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
