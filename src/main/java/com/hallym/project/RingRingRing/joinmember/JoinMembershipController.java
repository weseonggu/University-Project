package com.hallym.project.RingRingRing.joinmember;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.message.Message;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JoinMembershipController {
	
	private final JoinMembershipService joinService;
	
	@PostMapping("/signup")
	public ResponseEntity<Message> joinProcess(@Valid @RequestBody UserEntity userInfo) {
		
		return joinService.joinService(userInfo);
		
	}
	
	
	@GetMapping("/emailcheck/{email}")
	public ResponseEntity<?> EmailDuplicateVerificationController(@PathVariable("email") String email){
		
		return joinService.EmailDuplicateVerificationService(email);
		
	}
	@GetMapping("/find/{email}")
	public void findProcess(@PathVariable("email") String email) {
		
		joinService.find(email);
		
	}


}
