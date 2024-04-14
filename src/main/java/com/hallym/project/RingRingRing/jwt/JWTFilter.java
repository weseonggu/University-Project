package com.hallym.project.RingRingRing.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hallym.project.RingRingRing.Entity.AuthorityEntity;
import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.login.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 *  JWT토큰 유효성 검증 필터
 */
public class JWTFilter extends OncePerRequestFilter {


	/**
	 * 검증 메소드
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String jwt=null;
			try {
				jwt = request.getHeader("Authorization").split(" ")[1];
			} catch (NullPointerException e) {
				throw new MalformedJwtException("토큰이 없습니다.");
			}
			SecretKey key = new SecretKeySpec(JWTConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8),
					Jwts.SIG.HS256.key().build().getAlgorithm());
			Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();

			String username = String.valueOf(claims.get("username"));
			String roles = (String) claims.get("roles");

			String[] authoritiesArray = roles.split(",");
			Set<AuthorityEntity> authoritiesSet = new HashSet<>();

			for (String authority : authoritiesArray) {
				AuthorityEntity auth = AuthorityEntity.builder().role(authority).build();

				authoritiesSet.add(auth);
			}
			UserEntity userEntity = UserEntity.builder().email(username).pwd("temppassword").authorities(authoritiesSet)
					.build();

			CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

			Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
					customUserDetails.getAuthorities());
			logger.info("JWT validation Successful: "+username+" endpoint: "+request.getRequestURI());
			SecurityContextHolder.getContext().setAuthentication(authToken);

		} catch (MalformedJwtException e) {
			request.setAttribute("exception", e);
			logger.error("JWT validation failed: " + e.getMessage());
			return;
		} catch (SignatureException e) {
			request.setAttribute("exception", e);
			logger.error("JWT validation failed: " + e.getMessage());
			return;
		}catch (ExpiredJwtException e) {
			request.setAttribute("exception", e);
			logger.error("JWT validation failed: " + e.getMessage());
			return;
		}
		finally {
			
			filterChain.doFilter(request, response);
		}

	}

}
