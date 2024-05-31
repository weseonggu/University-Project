package com.hallym.project.RingRingRing.joinmember;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hallym.project.RingRingRing.joinmember.DTO.UserDTO;
import com.hallym.project.RingRingRing.jwt.CustomUserDetails;
import com.hallym.project.RingRingRing.message.CurrentTime;
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
	
	private final CurrentTime cTime;
	
	/**
	 * 회원가입 컨트롤러
	 */
	@PostMapping("/signup")
	@Operation(summary = "회원 가입 api")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "가입 성공"),
			@ApiResponse(responseCode = "409", description = "이미 사용중인 메일"),
			@ApiResponse(responseCode = "400", description = "유효성 없는 데이터 요청"),
			@ApiResponse(responseCode = "400", description = "이메일 인증 안함"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<SuccessMessage> joinProcess(@Valid @RequestBody UserDTO userInfo) {
		int result = joinService.joinService(userInfo);
		if(result == 1) {
			return new ResponseEntity<SuccessMessage>(new SuccessMessage(cTime.getTime(),"회원가입 완료"), HttpStatus.OK);
		}
		else if(result == 2){
			return new ResponseEntity<SuccessMessage>(new SuccessMessage(cTime.getTime(),"이미 사용중인 메일 입니다."), HttpStatus.CONFLICT);			
		}
		else if(result == 3){
			return new ResponseEntity<SuccessMessage>(new SuccessMessage(cTime.getTime(),"이메일 인증을 해주세요"), HttpStatus.BAD_REQUEST);			
		}
		else{
			return new ResponseEntity<SuccessMessage>(new SuccessMessage(cTime.getTime(),"서버 오류"), HttpStatus.INTERNAL_SERVER_ERROR);	
		}
	}
	
	/**
	 * 이메일 중복 검사 컨트롤러
	 */
	@GetMapping("/emailcheck/{email}")
	@Operation(summary = "이메일 중복검사 api", description = "임시 가입기능 제공")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "사용 가능한 이메일"),
			@ApiResponse(responseCode = "409", description = "사용중인 이메일")
	})
	public ResponseEntity<?> EmailDuplicateVerificationController(@PathVariable("email") String email){
		
		if(joinService.EmailDuplicateVerificationService(email)) {
			return new ResponseEntity<SuccessMessage>(new SuccessMessage(cTime.getTime(),"사용가능한 Email입니다."), HttpStatus.OK);
		}
		else{
			return new ResponseEntity<SuccessMessage>(new SuccessMessage(cTime.getTime(),"이미 사용중인 메일 입니다."), HttpStatus.CONFLICT);
		}
		
	}
	
	
	@Operation(summary = "회원 탈퇴 api", description = "그냥 만들어 봄")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "탈퇴 성공"),
			@ApiResponse(responseCode = "400", description = "비번이 틀리거나 다른이유로 실패")
	})
	@PostMapping("/userDelete")
	@PreAuthorize("isAuthenticated() && principal.getId() == #userinfo.getId()")
	public ResponseEntity<String> deleteUser(@RequestBody UserDTO userinfo){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		System.out.println(userDetails.getId());
		
		System.out.println(userinfo.getId());
		
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
