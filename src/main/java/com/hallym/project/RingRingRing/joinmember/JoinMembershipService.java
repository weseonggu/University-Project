package com.hallym.project.RingRingRing.joinmember;

import java.time.LocalDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hallym.project.RingRingRing.Mail.MailRepository;
import com.hallym.project.RingRingRing.joinmember.DTO.UserDTO;
import com.hallym.project.RingRingRing.joinmember.entity.AuthorityEntity;
import com.hallym.project.RingRingRing.joinmember.entity.TemporaryEmail;
import com.hallym.project.RingRingRing.joinmember.entity.UserEntity;
import com.hallym.project.RingRingRing.joinmember.repository.AuthorityRepository;
import com.hallym.project.RingRingRing.joinmember.repository.TemporaryEmailRepository;
import com.hallym.project.RingRingRing.joinmember.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 회원가입 요청을 처리하는 클래스
 */
@Service
@RequiredArgsConstructor
@EnableScheduling
@Transactional(readOnly = true)
public class JoinMembershipService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final AuthorityRepository authorityRepository;

	private final TemporaryEmailRepository temporaryEmailRepository;

	private final MailRepository mailRepository;
	/**
	 * 회원가입 서비스
	 * 
	 * @param userInfo 컨트롤러에서 받아온 UserEntity객체
	 * @return int 1: 성공, 2: 이미 사용중인 아이디 3: 이메일 검증 아나함
	 * @throws DataAccessException, RuntimeException
	 */
	@Transactional(rollbackFor = { RuntimeException.class, DataAccessException.class })
	public int joinService(UserDTO userInfo) {

		

		try {
			if (userRepository.existsByEmail(userInfo.getEmail())) {
				return 2;// 이미 가입자
			}

			boolean is = mailRepository.findByEmail(userInfo.getEmail(), Sort.by(Sort.Direction.DESC, "id")).get(0).isCheckCode();
			if (is == false) {
				return 3;// 이메일 검증 유무 확인
			}
			
			UserEntity user = UserEntity.builder().name(userInfo.getName()).email(userInfo.getEmail())
					.pwd(passwordEncoder.encode(userInfo.getPwd())).build();

			userRepository.save(user);
			AuthorityEntity authority = AuthorityEntity.builder().role("ROLE_AI_CALL")
					.user(userRepository.findByEmail(userInfo.getEmail()).get(0)).build();

			authorityRepository.save(authority);
			mailRepository.deleteByEmail(userInfo.getEmail());
			return 1;

		}catch (IndexOutOfBoundsException e) {
			return 3;
			
		}catch (DataAccessException e) {
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 이메일 중복 체크 임시로 이메일을 저장하여 가입중 다른 사용자가 가입 못하도록 함
	 * 
	 * @param email 문자열 컨트롤러에서 받아온 String
	 * @return boolean
	 * @throws DataAccessException, RuntimeException
	 */
	@Transactional(rollbackFor = { RuntimeException.class, DataAccessException.class })
	public boolean EmailDuplicateVerificationService(String email) {
		try {
			if (!userRepository.existsByEmail(email) && !temporaryEmailRepository.existsByEmail(email)) {
				TemporaryEmail tEmail = TemporaryEmail.builder().email(email).checkTime(LocalDateTime.now()).build();
				temporaryEmailRepository.save(tEmail);
				
				return true;
			} else {
				return false;
			}
			
		}catch (DataAccessException e) {
			throw new RuntimeException(e);
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}catch (Exception e) {
			throw e;
		}
		
	}

	/**
	 * 임시가입된 이메일정보를 10분마다 10분 전의 데이터를 지운다.
	 */
	@Transactional(rollbackFor = { RuntimeException.class, DataAccessException.class })
	@Scheduled(cron = "0 */10 * * * *")
	public void cleanupOldData() {
		try {
			LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
			temporaryEmailRepository.deleteOlderThanTenMinutes(tenMinutesAgo);

		} catch (DataAccessException e) {
			throw new RuntimeException(e);
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 사용자 삭제
	 */
	@Transactional(rollbackFor = { RuntimeException.class, DataAccessException.class })
	public int deleteUserService(UserDTO userInfo) {
		try {
			UserEntity user = userRepository.findById(userInfo.getId()).get();

			if (passwordEncoder.matches(userInfo.getPwd(), user.getPwd())) {
				userRepository.deleteById(userInfo.getId());
				return 2;
			} else {
				return 3;
			}
		} catch (DataAccessException e) {
			throw new RuntimeException(e);
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}catch (Exception e) {
			throw e;
		}

	}
	
	

}
