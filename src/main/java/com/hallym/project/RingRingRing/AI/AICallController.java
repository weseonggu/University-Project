package com.hallym.project.RingRingRing.AI;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AICallController {
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

}
