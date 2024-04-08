package com.hallym.project.RingRingRing.login;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hallym.project.RingRingRing.Entity.AuthorityEntity;
import com.hallym.project.RingRingRing.Entity.UserEntity;

public class CustomUserDetails implements UserDetails{
	private UserEntity userEntity;
	public CustomUserDetails(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		// UserEntity에서 권한을 가져와서 GrantedAuthority 객체로 변환하여 authorities에 추가
	    for (AuthorityEntity authority : userEntity.getAuthorities()) {
	        authorities.add(new SimpleGrantedAuthority(authority.getRole()));
	    }
	    return authorities;

	}
	@Override
	public String getPassword() {
		return userEntity.getPwd();
	}
	@Override
	public String getUsername() {
		return userEntity.getEmail();
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
}
