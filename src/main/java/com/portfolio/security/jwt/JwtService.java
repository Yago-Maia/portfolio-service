package com.portfolio.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class JwtService {
    @Value("${security.jwt.expiration}")
    private String expiration;

    @Value("${security.jwt.signature-key}")
    private String signatureKey;

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(signatureKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            LocalDateTime date = getClaims(token).getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(date);
        } catch (Exception ex) {
            return false;
        }
    }

    public String getEmailUser(String token) throws ExpiredJwtException {
        return (String) getClaims(token).getSubject();
    }

    public String getRole(String token) throws ExpiredJwtException {
        return (String) getClaims(token).get("role");
    }

    public Long getUserId(String token) throws ExpiredJwtException {
        return ((Integer) getClaims(token).get("idUser")).longValue();
    }
}
