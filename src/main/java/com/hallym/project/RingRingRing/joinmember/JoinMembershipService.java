package com.hallym.project.RingRingRing.joinmember;

import java.time.LocalDate;
import java.util.List;

import com.hallym.project.RingRingRing.repository.ScenarioRepository;
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
import com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinMembershipService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final AuthorityRepository authorityRepository;

	private final ScenarioRepository scenarioRepository;

	public ResponseEntity<Message> joinService(UserEntity userInfo) {
		
		if(userRepository.existsByEmail(userInfo.getEmail())) {
			throw new IDOverlapException("already have an ID. Please use a different ID");
		}
		
		try {
			UserEntity user = UserEntity.builder()
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

	public ResponseEntity<Message> EmailDuplicateVerificationService(String email) {
		if (!userRepository.existsByEmail(email)) {
			return new ResponseEntity<Message>(new Message(LocalDate.now(), "사용 가능한 Email입니다."), HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<Message>(new Message(LocalDate.now(), "사용 불가능한 Email입니다."), HttpStatus.CONFLICT);
		}
	}
	
	public void find(String email){
//		return <UserEntity>(userRepository.findByEmail(email).get(0), HttpStatus.ACCEPTED);
		System.out.println("사용자이름:"+userRepository.findByEmail(email).get(0).getEmail());
		System.out.println("비번: "+userRepository.findByEmail(email).get(0).getPwd());
		
		for (AuthorityEntity a : userRepository.findByEmail(email).get(0).getAuthorities()) {
		    System.out.println("권한: "+a.getRole());
		}
		
	}

	public List<WeeklyUsageDTO> getWeeklyUsageById(Long id){
		return scenarioRepository.findWeeklyUsageById(id);
	}





}
