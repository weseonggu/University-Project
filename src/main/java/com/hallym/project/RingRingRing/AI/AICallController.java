package com.hallym.project.RingRingRing.AI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hallym.project.RingRingRing.DTO.Conversation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AICallController {
	
	private final AICallService aICallService;
	/**
	 * AI_CALL 권한이 있는 사용자만이 접근이 가능
	 * @param talk
	 * @return
	 */
	@GetMapping("/aicall")
	public String call(String talk) {
		
		
		
		// 
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // ai가 만든 대화
        
        return "전화 왔습니다.  : "+name;
	}
	@PostMapping("kogpt2")
	public ResponseEntity<String> createSentence(@RequestBody Conversation talk_data ) {
		String newSentence = aICallService.fastAPIRequest(talk_data);
		return new ResponseEntity<String>(newSentence,HttpStatus.OK);
	}
}
