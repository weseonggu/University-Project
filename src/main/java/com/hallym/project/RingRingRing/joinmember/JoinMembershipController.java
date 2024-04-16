package com.hallym.project.RingRingRing.joinmember;

import com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.message.Message;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원가입 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class JoinMembershipController {
	
	private final JoinMembershipService joinService;
	
	/**
	 * 회원가입 컨트롤러
	 *요청 방식: POST
	 *EndPoint" /singnup
	 * @param userInfo 사용자 정보로 {"name":"", "email":"", "pwd":""}를 body에 넣어서 요청 
	 * @return CODE:202 BODY: {"date": "2024-04-14","massage": "Sueccess"} OR CODE:409 BODY: {"date": "2024-04-14","massage": "Fail"}
	 * 이미 사용중인 이메일 일경우 IDOverlapException
	 * 유형성 검사에 틀리면 MethodArgumentNotValidException
	 */
	@PostMapping("/signup")
	public ResponseEntity<Message> joinProcess(@Valid @RequestBody UserEntity userInfo) {
		
		return joinService.joinService(userInfo);
		
	}
	
	/**
	 * 요청 방식: GET
	 * url: /emailcheck/입력한 이메일
	 * @param email 주소에 입력한 이메일  
	 * @return CODE: 409 BODY: {"date": "2024-04-14","massage": "사용 불가능한 Email입니다."} OR CODE: 202 BODY: {"date": "2024-04-14","massage": "사용 가능한 Email입니다."}
	 */
	@GetMapping("/emailcheck/{email}")
	public ResponseEntity<?> EmailDuplicateVerificationController(@PathVariable("email") String email){
		
		return joinService.EmailDuplicateVerificationService(email);
		
	}
	
	@GetMapping("/find/{email}")
	public void findProcess(@PathVariable("email") String email) {
		
		joinService.find(email);
		
	}

	@GetMapping("/usage/{email}")
	public ResponseEntity<List<WeeklyUsageDTO>> getWeeklyUsage(@PathVariable("email") String email){
		List<WeeklyUsageDTO> weeklyusages = joinService.getWeeklyUsageByEmail(email);
		return ResponseEntity.ok(weeklyusages);
	}



}
