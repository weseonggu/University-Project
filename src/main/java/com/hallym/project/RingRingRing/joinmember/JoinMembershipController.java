package com.hallym.project.RingRingRing.joinmember;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hallym.project.RingRingRing.DTO.UserDTO;
import com.hallym.project.RingRingRing.customexception.IDOverlapException;
import com.hallym.project.RingRingRing.message.SuccessMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 회원가입 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@Tag(name="회원 컨트롤러", description = "가입 탈퇴 관련 컨트롤러")
public class JoinMembershipController {
	
	private final JoinMembershipService joinService;
//	private final UserRepository userRepository;
	
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
	@Operation(summary = "회원 가입 api")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "가입 성공"),
			@ApiResponse(responseCode = "409", description = "이미 사용중인 메일"),
			@ApiResponse(responseCode = "404", description = "가입 실패"),
			@ApiResponse(responseCode = "400", description = "유효성 없는 데이터 요청")
	})
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
	@Operation(summary = "이메일 중복검사 api", description = "임시 가입기능 제공")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "사용 가능한 이메일"),
			@ApiResponse(responseCode = "409", description = "사용중인 이메일")
	})
	public ResponseEntity<?> EmailDuplicateVerificationController(@PathVariable("email") String email){
		
		return joinService.EmailDuplicateVerificationService(email);
		
	}
	
	
	@Operation(summary = "회원 탈퇴 api", description = "그냥 만들어 봄")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "탈퇴 성공"),
			@ApiResponse(responseCode = "400", description = "비번이 틀리거나 다른이유로 실패")
	})
	@PostMapping("userDelete")
	public ResponseEntity<String> deleteUser(@RequestBody UserDTO userinfo){
		
		int resault = joinService.deleteUserService(userinfo);
		
		if(resault == 2) {
			return new ResponseEntity<String>("계정이 삭제되었습니다.", HttpStatus.OK);
		}else if(resault == 3) {
			return new ResponseEntity<String>("비번이 클립니다.", HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<String>("계정 삭제를 못했습니다. 다시 시도해주세요", HttpStatus.BAD_REQUEST);
		}
	}
	





}
