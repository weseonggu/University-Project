package com.hallym.project.RingRingRing.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hallym.project.RingRingRing.error.TokenPeriodException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

    // 토큰 생성
    public String createJwt(String username, String roles, Long expiredMs) {
    	
    	SecretKey secretKey = Keys.hmacShaKeyFor(JWTConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claim("username", username)
                .claim("roles", roles) // 역할 정보를 JSON 배열로 설정
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
