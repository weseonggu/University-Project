package com.hallym.project.RingRingRing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hallym.project.RingRingRing.filter.LoginFilter;
import com.hallym.project.RingRingRing.jwt.JWTFilter;
import com.hallym.project.RingRingRing.jwt.JWTUtil;

import lombok.RequiredArgsConstructor;
/**
 * 스필이 시큐리 설정 빈 
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class RingRRSecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final AuthenticationEntryPoint entryPoint;
	private final JWTUtil jwtUtil;


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	// AuthenticationManager Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable());

		http.formLogin(login -> login.disable());

		http.httpBasic(basic -> basic.disable());

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.addFilterAfter(new JWTFilter(), LoginFilter.class);

		http.authorizeHttpRequests((auth) -> auth
				.requestMatchers( "/login","/signup", "/emailcheck/**","/find/**").permitAll()
				.requestMatchers( "/v3/**", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()// swagger
				.requestMatchers("/aicall").hasRole("AI_CALL")
				.requestMatchers("/kogpt2").hasRole("AI_CALL")
				.anyRequest().authenticated());
		http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint));

		return http.build();
	}

}

