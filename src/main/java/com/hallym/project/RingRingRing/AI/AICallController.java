package com.hallym.project.RingRingRing.AI;

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

import com.hallym.project.RingRingRing.jwt.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AICallController {
	
	private final AICallService aICallService;
	
	/**
	 * AI_CALL 권한이 있는 사용자만이 접근이 가능
	 * @param talk
	 * @return
	 */
//	@GetMapping("/aicall/{email}")
//	@PreAuthorize("isAuthenticated() && principal.username == #email")
//	public String call(@PathVariable("email") String email) {
//		
//	    log.info("접근 성공");
//	    String name = SecurityContextHolder.getContext().getAuthentication().getName();
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//	    Long id = userDetails.getUserEntity().getId();
//	    return "id: "+ userDetails.getId() +" email: "+name;
//	}
	@GetMapping("/aicall/{id}")
	@PreAuthorize("isAuthenticated() && principal.getId() == #id")
	public String call(@PathVariable("id") Long id) {
		
	    log.info("접근 성공");
	    String name = SecurityContextHolder.getContext().getAuthentication().getName();
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	    return "id: "+ userDetails.getId() +" email: "+name;
	}
	@PostMapping("/deliveryAI")
	@Operation(summary = "배달 ai api")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "생성된 문장")
	})
	public ResponseEntity<String> createSentence(@RequestBody Conversation talk_data ) {
		String newSentence = aICallService.fastAPIRequest(talk_data);
		return new ResponseEntity<String>(newSentence,HttpStatus.OK);
	}
	
	@GetMapping("/isconnected")
	@PostMapping("/deliveryAI")
	@Operation(summary = "서버 연결 확인 api")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "연결되었습니다.")
	})
	public String check() {
		
		return aICallService.fastAPIIsConnected();
	}
}
