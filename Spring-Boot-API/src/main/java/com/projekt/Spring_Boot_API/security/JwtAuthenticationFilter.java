package com.projekt.Spring_Boot_API.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.projekt.Spring_Boot_API.exceptions.user.UserNotFoundException;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
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
        System.out.println("=== JWT Filter Debug ===");
        System.out.println("Request URI: " + request.getRequestURI());

        String header = request.getHeader("Authorization");
        System.out.println("Auth Header: " + header);

        if (header == null || header.isBlank() || !header.startsWith("Bearer ")) {
            System.out.println("No valid Bearer token, continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring("Bearer ".length());
        System.out.println("Token extracted: " + token.substring(0, Math.min(20, token.length())) + "...");

        UUID userId;
        try {
            userId = jwtService.validateToken(token);
            System.out.println("Token validated, userId: " + userId);
        } catch (JWTVerificationException e) {
            System.out.println("Token validation failed: " + e.getMessage());
            response.setStatus(401);
            return;
        }

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isEmpty()) {
            System.out.println("User not found for userId: " + userId);
            response.setStatus(401);
            return;
        }

        User user = optionalUser.get();
        System.out.println("User found: " + user.getUsername());
        System.out.println("Authorities: " + user.getAuthorities());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        System.out.println("Authentication set in SecurityContext");
        System.out.println("Is Authenticated: " + authToken.isAuthenticated());
        System.out.println("=== End JWT Filter Debug ===");

        filterChain.doFilter(request, response);
    }
}
