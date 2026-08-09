package br.com.giovaniramires.kanban_spring.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;



@Component
public class JwtUtil {
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(String email){
        return Jwts.builder()
        .subject(email)
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
        .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
        .compact();
    }

    public String validarToken(String token){
        try {
            return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
