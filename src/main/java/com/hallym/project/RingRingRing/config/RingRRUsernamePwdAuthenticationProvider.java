package com.hallym.project.RingRingRing.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class RingRRUsernamePwdAuthenticationProvider implements AuthenticationProvider{
	/*
	 * 사용자 로그인 시 사용되는 메서드
	 * @pram authentication 
	 * @return 객체
	 * */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/*
	 * 
	 * */
	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return false;
	}

}
