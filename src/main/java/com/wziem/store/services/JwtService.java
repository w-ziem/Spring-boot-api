package com.wziem.store.services;

import com.wziem.store.config.JwtConfig;
import com.wziem.store.entities.Role;
import com.wziem.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@AllArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    public String generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }


    public String generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    private String generateToken(User user, long tokenExpirationTime) {
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpirationTime * 1000))
                .signWith(jwtConfig.getSecretKey())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .compact();
    }

    public boolean validateToken(String token) {
       try {
           var claims = getClaims(token);

           return claims.getExpiration().after(new Date());

       } catch (Exception e) {
           //if any exception is caught token is invalid
           return false;
       }
    }

    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload() ;
    }


    public Long getIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public Role getRoleFromToken(String token) {
        return Role.valueOf(getClaims(token).get("role", String.class));
    }
}
