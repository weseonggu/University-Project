package com.hallym.project.RingRingRing.login;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.repository.UserRepository;

import lombok.RequiredArgsConstructor;
/**
 * DB기반 검증을 위한 UserDetailsService 구현 클래스
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetilasService implements UserDetailsService{
	
	private final UserRepository userRepository;
	/**
	 * UserDetailsService 구현 클래스 DBrlqks 검증 수행
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	    List<UserEntity> userDatas = userRepository.findByEmail(username);
	    if (userDatas.isEmpty()) {
	    	System.out.println("loadUserByUsername");
	        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." );
	    } else {
	        return new CustomUserDetails(userDatas.get(0));
	    }
		
		
		
	}

}
