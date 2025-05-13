package com.learn.authService.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    private final String SECRET_KEY = "your-secret-key-here-replace-with-a-secure-key-of-at-least-256-bits-length-for-hmac-sha256";

    private final long JWT_EXPIRATION_MS= 1000 * 60 * 60 * 1; // 1 hour

    private Key signingKey;
    private JwtParser jwtParser;

    public JwtService() {
        try {
            System.out.println("jwt secret: " + SECRET_KEY);
            System.out.println("jwt expiration: " + JWT_EXPIRATION_MS);
            String base64EncodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
            this.signingKey = Keys.hmacShaKeyFor(base64EncodedKey.getBytes());
            this.jwtParser = Jwts.parser().verifyWith((SecretKey) signingKey).build();
        }catch (Exception e) {
            System.out.println("Error initializing JWT service: " + e.getMessage());
        }

    }

    public String generateToken(String username) {
        return generateToken(username, null);
    }

    public String generateToken(String username, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        JwtBuilder builder = Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256);

        return builder.compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isTokenValid(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }
}