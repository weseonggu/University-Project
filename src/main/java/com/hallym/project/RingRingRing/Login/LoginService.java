package com.hallym.project.RingRingRing.Login;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.hallym.project.RingRingRing.joinmember.DTO.UserDTO;
import com.hallym.project.RingRingRing.joinmember.entity.AuthorityEntity;
import com.hallym.project.RingRingRing.joinmember.entity.UserEntity;
import com.hallym.project.RingRingRing.joinmember.repository.UserRepository;
import com.hallym.project.RingRingRing.jwt.JWTUtil;
import com.hallym.project.RingRingRing.message.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final JWTUtil jwtUtil;
	
	
	/**
	 * 로그인 프로세스
	 * @param user_info 사용자의 이메일과 비번이 담긴 DTO
	 * @return ResponseEntity<?>
	 * @throws RuntimeException
	 */
	public ResponseEntity<?> loginService(LoginDTO user_info) {
		try {
			
			UserEntity user_info_at_DB = userRepository.findByEmail(user_info.getEmail()).get(0);
			
					
			
			
			
			if(isExistsUser(user_info, user_info_at_DB.getPwd())) {
				String roles = populateAuthorities(user_info_at_DB.getAuthorities());
				
				
				// 토큰 생성 만료시간 30000000 8시간 정도
				String token = jwtUtil.createJwt(user_info_at_DB.getEmail(), roles, 30000000L);
				UserDTO user = new UserDTO(
						user_info_at_DB.getId()
						,user_info_at_DB.getName()
						,user_info_at_DB.getEmail()
						,user_info_at_DB.getPwd());
				
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
				
				SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "email");
				
				FilterProvider filters =  new SimpleFilterProvider().addFilter("loginFilter", filter);
				
				mappingJacksonValue.setFilters(filters);
				
				HttpHeaders headers = new HttpHeaders();
		        headers.add("Authorization", "Bearer " + token);

				
				return new ResponseEntity<MappingJacksonValue>(mappingJacksonValue, headers, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<Message>(new Message(LocalDate.now(), "아이디 또는 비번이 틀렸습니다."),HttpStatus.UNAUTHORIZED);
			}
		}catch (IndexOutOfBoundsException e) {
			return new ResponseEntity<String>("아이디나 또는 비번이 틀렸습니다.",HttpStatus.UNAUTHORIZED);
		}
		catch (RuntimeException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
		catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	
	
	

	/**
	 * 
	 * @param user_info 로그인 시 입력한 정보 객체
	 * @param encoded_pwd DB에 있는 인코딩된 비번
	 * @return boolean 사용자가 있고 비번이 맞으면 true 하나라도 아니면 false
	 */
	private boolean isExistsUser(LoginDTO user_info, String encoded_pwd) {
		
		if(userRepository.existsByEmail(user_info.getEmail()) && passwordEncoder.matches(user_info.getPwd(), encoded_pwd)) {
			// 사용자 있음, 비번 맞음
			return true;
		}
		else {
			// 사용자 없음 또는 비번이 틀림
			return false;
		}
	}
	/**
	 * 권한 문자영로 변경
	 * @param collection
	 * @return 권한1, 권한2...의 문자열
	 */
	private String populateAuthorities(Set<AuthorityEntity> collection) {
		Set<String> authoritiesSet = new HashSet<>();
		for (AuthorityEntity authority : collection) {
			authoritiesSet.add(authority.getRole());
		}
		return String.join(",", authoritiesSet);
	}
}
