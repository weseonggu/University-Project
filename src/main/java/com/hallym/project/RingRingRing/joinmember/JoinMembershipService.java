package com.hallym.project.RingRingRing.joinmember;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO;
import com.hallym.project.RingRingRing.Entity.AuthorityEntity;
import com.hallym.project.RingRingRing.Entity.TemporaryEmail;
import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.customexception.IDOverlapException;
import com.hallym.project.RingRingRing.customexception.JoinFailException;
import com.hallym.project.RingRingRing.message.CurrentTime;
import com.hallym.project.RingRingRing.message.SuccessMessage;
import com.hallym.project.RingRingRing.repository.AuthorityRepository;
import com.hallym.project.RingRingRing.repository.TemporaryEmailRepository;
import com.hallym.project.RingRingRing.repository.UserRepository;
import com.hallym.project.RingRingRing.repository.WeeklyUsageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * 회원가입 요청을 처리하는 클래스
 */
@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class JoinMembershipService {
	

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final AuthorityRepository authorityRepository;
	
	private final TemporaryEmailRepository temporaryEmailRepository;

	
	private final CurrentTime cTime;
	
	private final WeeklyUsageRepository weeklyUsageRepository;

	/**
	 * 회원가입 서비스
	 * @param userInfo 컨트롤러에서 받아온 UserEntity객체 
	 * @return ResponseEntity<SuccessMessage>
	 * @throws IDOverlapException, JoinFailException
	 */
	public ResponseEntity<SuccessMessage> joinService(UserEntity userInfo) {
		
		if(userRepository.existsByEmail(userInfo.getEmail())) {
			throw new IDOverlapException("이미 사용중인 Email입니다.");
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
			
			return new ResponseEntity<SuccessMessage>(new SuccessMessage(cTime.getTime(),"회원가입 완료"), HttpStatus.OK);
			
		}catch (Exception e) {
			throw new JoinFailException("회원 가입 실패");
		}
		
		
	}
	
	/**
	 * 이메일 중복 체크
	 * 임시로 이메일을 저장하여 가입중 다른 사용자가 가입 못하도록 함
	 * @param email 문자열 컨트롤러에서 받아온 String
	 * @return ResponseEntity<SuccessMessage>
	 * @throws IDOverlapException
	 */
	public ResponseEntity<SuccessMessage> EmailDuplicateVerificationService(String email) {
		
		
		if (!userRepository.existsByEmail(email) && !temporaryEmailRepository.existsByEmail(email) ) {
			TemporaryEmail tEmail = TemporaryEmail.builder()
					.email(email)
					.checkTime(LocalDateTime.now())
					.build();
			temporaryEmailRepository.save(tEmail);
			
			return new ResponseEntity<SuccessMessage>(new SuccessMessage(cTime.getTime(),"사용가능한 Email입니다."), HttpStatus.OK);
		} else {
			throw new IDOverlapException("이미 사용중인 Email입니다.");
		}
	}
	
	
	/**
	 * 임시가입된 이메일정보를 10분마다 10분 전의 데이터를 지운다.
	 */
	 @Scheduled(cron = "0 */10 * * * *")
	 public void cleanupOldData() {
		 try {
			 LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
			 temporaryEmailRepository.deleteOlderThanTenMinutes(tenMinutesAgo);
			 log.info("임시 가입 메일 지움");
			
		} catch (Exception e) {
			log.warn("임시 가입 메일 지우기 실패: " + e.getMessage());
		}
	 }
	


	public List<WeeklyUsageDTO> getWeeklyUsageByEmail(String email){
		return weeklyUsageRepository.findWeeklyUsageByEmail(email);
	}





}
