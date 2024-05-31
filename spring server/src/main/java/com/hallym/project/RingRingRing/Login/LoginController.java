package com.hallym.project.RingRingRing.Login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name="로그인 컨트롤러", description = "로그인 관련 컨트롤러")
public class LoginController {
	
	private final LoginService loginService;
	
	@PostMapping("/login")
	@Operation(summary = "로그인 api")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "401", description = "아이디 또는 비번이 클림"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<?> loginController(@RequestBody LoginDTO user_info) {
		
		return loginService.loginService(user_info);
		
	}
}
