package com.projekt.Spring_Boot_API.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * This service handles generating and validating JWTs.
 *
 * @author Elina Samuelsson
 * @version 1.0
 */
@Service
public class JwtService {
    /**
     * Generates an algorithm based on a key stored as an environment variable.
     */
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(System.getenv("JWT_SECRET"));

    /**
     * Uses the algorithm to create a validator for decoding JWTs.
     */
    private static final JWTVerifier verifier = JWT
            .require(ALGORITHM)
            .withIssuer("Spring-Boot-API").build();

    /**
     * Uses the algorithm to generate a unique JWT to be used as a user's identification
     * towards the application whenever logged in.
     *
     * @param userId takes in the user id of the user who needs a token
     * @return a String with the generated token
     */
    public String generateToken(UUID userId) {
        return JWT
                .create()
                .withIssuer("Spring-Boot-API")
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(60, ChronoUnit.MINUTES))
                .withSubject(userId.toString())
                .sign(ALGORITHM);
    }

    /**
     * Validates a token on request.
     *
     * @param token takes in the token to be validated in the form of a String
     * @return the UUID of the user from the decoded token
     */
    public UUID validateToken(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
        return UUID.fromString(decodedJWT.getSubject());
    }
}
