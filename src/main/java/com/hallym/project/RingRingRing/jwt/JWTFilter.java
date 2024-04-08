package com.hallym.project.RingRingRing.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hallym.project.RingRingRing.Entity.AuthorityEntity;
import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.login.CustomUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    		throws ServletException, IOException {
				
				//request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");
				
				//Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;

						
        }
			
				//Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
			
				//토큰 소멸 시간 검증
        System.out.println("토큰 마료?: "+jwtUtil.isExpired(token));
        try {
        	if (jwtUtil.isExpired(token)) {
        		filterChain.doFilter(request, response);
        		response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 응답 코드 설정
        		response.getWriter().println("Forbidden: Token expired"); // 응답 본문에 메시지 출력
        	}
        	
        }catch (ExpiredJwtException e) {
        	System.out.println("토큰시간 만료");

		}


        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        
        String[] authoritiesArray = role.split(",");
        Set<AuthorityEntity> authoritiesSet = new HashSet<>();
        
        for (String authority : authoritiesArray) {
        	System.out.println(authority);
        	AuthorityEntity auth = AuthorityEntity
        			.builder()
        			.role(authority)
        			.build();
        	
            authoritiesSet.add(auth);
        }
				
        UserEntity userEntity =UserEntity.builder()
        		.email(username)
        		.pwd("temppassword")
        		.authorities(authoritiesSet)
        		.build();
        
				
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        

        filterChain.doFilter(request, response);
    }
  // 이거랑 비교해서 다시 만들기
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
//        if (null != jwt) {
//            try {
//                SecretKey key = Keys.hmacShaKeyFor(
//                        SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
//
//                Claims claims = Jwts.parser()
//                        .verifyWith(key)
//                        .build()
//                        .parseSignedClaims(jwt)
//                        .getPayload();
//                String username = String.valueOf(claims.get("username"));
//                String authorities = (String) claims.get("authorities");
//                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
//                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            } catch (Exception e) {
//                throw new BadCredentialsException("Invalid Token received!");
//            }
//
//        }
//        filterChain.doFilter(request, response);
//    }

}
