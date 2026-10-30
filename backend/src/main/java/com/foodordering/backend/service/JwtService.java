package com.foodordering.backend.service;

import javax.crypto.SecretKey;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foodordering.backend.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expiration;

    public JwtService( @Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) 
    {
        this.signingKey = Keys.hmacShaKeyFor( Decoders.BASE64.decode(secret));
        this.expiration = expiration;
    }

    public String generateToken(User user) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey)
                .compact();

    }

    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, User user) {

        try {
            String email = extractEmail(token);

            return email.equals(user.getEmail());

        } catch (JwtException | IllegalArgumentException exception) {

            return false;
        }
    }
}
