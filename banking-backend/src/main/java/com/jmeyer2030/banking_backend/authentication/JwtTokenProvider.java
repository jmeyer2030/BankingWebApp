package com.jmeyer2030.banking_backend.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    public JwtTokenProvider() { }

    @PostConstruct
    public void init() {
        // Decode the base64 secret into a byte array
        byte[] arr = Base64.getDecoder().decode(jwtSecret);

        // Create a new secretKey from the decoded secret
        this.secretKey = Keys.hmacShaKeyFor(arr);
    }

    /**
    * Generates a token that cannot successfully be tampered with without the SecretKey
    * It stores:
    *  - A username
    *  - Issue time
    *  - Expiration time
    */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    /**
    * Ensures the following:
    *  - The token hasn't been tampered with
    *  - The token has the correct secret key
    *  - The token hasn't expired
    */

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
    * Returns the username of an encrypted token
    * Probably throws an exception if the token is bad.
    *
    */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

}
