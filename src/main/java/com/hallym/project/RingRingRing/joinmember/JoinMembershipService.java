package com.hallym.project.RingRingRing.joinmember;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hallym.project.RingRingRing.Entity.AuthorityEntity;
import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.customexception.IDOverlapException;
import com.hallym.project.RingRingRing.message.Message;
import com.hallym.project.RingRingRing.repository.AuthorityRepository;
import com.hallym.project.RingRingRing.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinMembershipService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final AuthorityRepository authorityRepository;
	
	
	/**
	 * 회원가입 서비스
	 * @param userInfo 컨트롤러에서 받아온 UserEntity객체 
	 * @return 실패 or 성공
	 */
	public ResponseEntity<Message> joinService(UserEntity userInfo) {
		
		if(userRepository.existsByEmail(userInfo.getEmail())) {
			throw new IDOverlapException("이미 사용중인 메일 입니다. 다른 메일을 사용해주세요");
		}
		
		try {
			UserEntity user = UserEntity.builder()
					.name(userInfo.getName())
					.email(userInfo.getEmail())
					.pwd(passwordEncoder.encode(userInfo.getPwd()))
					.build();
			
			userRepository.save(user);
			
			AuthorityEntity authority = AuthorityEntity.builder()
					.role("ROLE_AI_CALL")
					.user(userRepository.findByEmail(userInfo.getEmail()).get(0))
					.build();
			
			authorityRepository.save(authority);
			

			return new ResponseEntity<Message>(new Message(LocalDate.now(), "Sueccess"), HttpStatus.ACCEPTED);
			
		}catch (Exception e) {

			return new ResponseEntity<Message>(new Message(LocalDate.now(), "Fail"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	/**
	 * 이메일 중복 체크
	 * @param email 문자열 컨트롤러에서 받아온 String
	 * @return 실패 or 성공
	 */
	public ResponseEntity<Message> EmailDuplicateVerificationService(String email) {
		if (!userRepository.existsByEmail(email)) {
			return new ResponseEntity<Message>(new Message(LocalDate.now(), "사용 가능한 Email입니다."), HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<Message>(new Message(LocalDate.now(), "사용 불가능한 Email입니다."), HttpStatus.CONFLICT);
		}
	}
	
	public void find(String email){
//		return <UserEntity>(userRepository.findByEmail(email).get(0), HttpStatus.ACCEPTED);
		System.out.println("사용자이믈:"+userRepository.findByEmail(email).get(0).getEmail());
		System.out.println("비번: "+userRepository.findByEmail(email).get(0).getPwd());
		
		for (AuthorityEntity a : userRepository.findByEmail(email).get(0).getAuthorities()) {
		    System.out.println("권한: "+a.getRole());
		}
		
	}



}
