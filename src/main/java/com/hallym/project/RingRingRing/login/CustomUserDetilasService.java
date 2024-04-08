package com.hallym.project.RingRingRing.login;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CustomUserDetilasService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	    List<UserEntity> userDatas = userRepository.findByEmail(username);
	    if (userDatas.isEmpty()) {
	        throw new UsernameNotFoundException("UserNotFound" );
	    } else {
	        return new CustomUserDetails(userDatas.get(0));
	    }
		
		
		
	}

}
