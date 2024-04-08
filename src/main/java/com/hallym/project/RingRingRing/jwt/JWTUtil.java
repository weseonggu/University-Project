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

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secretkey}")String secret) {


        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("roles",String.class);
    }
    


    public Boolean isExpired(String token) {
    	try {
    		
    		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    		
    	}catch (TokenPeriodException e) {
			throw new TokenPeriodException("토큰 기간 만료");
		}
    }
    
    // 토큰 생성
    public String createJwt(String username, String roles, Long expiredMs) {
    	
    	
        return Jwts.builder()
                .claim("username", username)
                .claim("roles", roles) // 역할 정보를 JSON 배열로 설정
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
