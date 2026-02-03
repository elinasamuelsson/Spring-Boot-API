package com.projekt.Spring_Boot_API.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class JwtService {
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("secret");
    private static final JWTVerifier verifier = JWT
            .require(ALGORITHM)
            .withIssuer("Spring-Boot-API")
            .build();

    public String generateToken(UUID userId) {
        return JWT.create()
                .withIssuer("Spring-Boot-API")
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .withSubject(userId.toString())
                .sign(ALGORITHM);
    }

    public UUID validateToken(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
        return UUID.fromString(decodedJWT.getSubject());
    }
}
