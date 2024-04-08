package com.hallym.project.RingRingRing.AI;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AICallController {
	
	@GetMapping("/aicall")
	public String call() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return "전화 왔습니다.  : "+name;
	}

}
