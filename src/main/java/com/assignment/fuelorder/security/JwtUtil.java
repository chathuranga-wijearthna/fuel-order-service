package com.assignment.fuelorder.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Key key;
    private final Long ttlMillis;

    public JwtUtil(@Value("${app.jwt-secret}") String secret, @Value("${app.token-ttl}") Long ttlMillis) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.ttlMillis = ttlMillis;
    }

    public String generateToken(Authentication auth) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("roles", auth.getAuthorities())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttlMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
