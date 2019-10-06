package com.example.library.security;

import com.example.library.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("300000")
    private String expirationTime;

    @Autowired
    private SecretKey secretKey;

    public Claims getAllClaimsForToken(String token ) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameForToken( String token ) {
        return getAllClaimsForToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token ) {
        return getAllClaimsForToken(token).getExpiration();
    }

    public String generateToken(User user) {
        Map<String, Object> claims = Map.of("role", user.getRoles());
        return doTokenGeneration(claims, user.getUsername());
    }

    public boolean validateToken( String token ) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String doTokenGeneration(Map<String, Object> claims, String username) {
        Long expirationTimeLong = Long.parseLong(expirationTime);
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }
}
