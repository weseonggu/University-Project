package com.hallym.project.RingRingRing.joinmember;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hallym.project.RingRingRing.DTO.UserDTO;
import com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO;
import com.hallym.project.RingRingRing.customexception.IDOverlapException;
import com.hallym.project.RingRingRing.message.SuccessMessage;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 회원가입 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class JoinMembershipController {
	
	private final JoinMembershipService joinService;
	
	/**
	 * 회원가입 컨트롤러<br>
	 * 요청 방식: POST<br>
	 * EndPoint: /singnup<br>
	 * @param userInfo 사용자 정보로 {"name":"", "email":"", "pwd":""}를 body에 넣어서 요청 <br>
	 * @return CODE:200 BODY: {"date": LocalDateTime,"massage": "회원가입 완료"}
	 * @throws IDOverlapException CODE:409 BODY: {"timeStamp": "yyyy-MM-dd HH:mm:ss","endPoint": "uri=/signup","errorDetails":["이미 사용중인 Email입니다."]}<br>
	 * MethodArgumentNotValidException <br>
	 * CODE:400 BODY: {"timeStamp": "yyyy-MM-dd HH:mm:ss","endPoint": "uri=/signup","errorDetails":["이름을 입력해주세요", "이메일을 입력해주세요", "이메일 형식으로 입력해 주세요", "8자 이상글자에 적어도 하나의 특수문자, 영문자, 숫자를 포함해야 합니다."]}<br>
	 * 유효성 검증 실패
	 * UserEntity클래스 참고
	 */
	@PostMapping("/signup")
	public ResponseEntity<SuccessMessage> joinProcess(@Valid @RequestBody UserDTO userInfo) {
		
		return joinService.joinService(userInfo);
		
	}
	
	/**
	 * 요청 방식: GET<br>
	 * EndPoint: /emailcheck/만들려는 이메일<br>
	 * @param email 주소에 입력한 이메일<br>
	 * @return CODE: 200 BODY: {"timeStamp": "yyyy-MM-dd HH:mm:ss,"message": "사용가능한 Email입니다."} <br> 
	 * CODE: 409 BODY: {"timeStamp": "yyyy-MM-dd HH:mm:ss","endPoint": "uri=/emailcheck/메일","errorDetails":["이미 사용중인 Email입니다."]}
	 */
	@GetMapping("/emailcheck/{email}")
	public ResponseEntity<?> EmailDuplicateVerificationController(@PathVariable("email") String email){
		
		return joinService.EmailDuplicateVerificationService(email);
		
	}


	@GetMapping("/usage/{email}")
	public ResponseEntity<List<WeeklyUsageDTO>> getWeeklyUsage(@PathVariable("email") String email){
		List<WeeklyUsageDTO> weeklyusages = joinService.getWeeklyUsageByEmail(email);
		return ResponseEntity.ok(weeklyusages);
	}



}
