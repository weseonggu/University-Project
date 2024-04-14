package com.hallym.project.RingRingRing.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
/**
 * jwt 토큰 생성하는 클래스
 */
@Component
public class JWTUtil {

	/**
	 * 토큰 생성 메소드
	 * @param username 이메일  
	 * @param roles 권한
	 * @param expiredMs 토큰 유효 시간 8시간 정도
	 * @return
	 */
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
