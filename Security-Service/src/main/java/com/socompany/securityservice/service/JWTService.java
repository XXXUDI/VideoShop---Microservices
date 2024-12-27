package com.socompany.securityservice.service;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTService {

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${jwt.security.key}")
    private String SECRET;

    @SuppressWarnings("deprecation")
    public void validateToken(final String token) {
        Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }


    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    
    @SuppressWarnings("deprecation")
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        LOGGER.debug(SECRET.isEmpty() ? "Secret key is empty" : "Secret key is valid");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @PostConstruct
    public void init() {
        if( SECRET == null || SECRET.trim().isEmpty()) {
            throw new RuntimeException("Secret key is empty");
        } else {
            LOGGER.debug("Secret key is successfully initialized");
        }
    }

}
