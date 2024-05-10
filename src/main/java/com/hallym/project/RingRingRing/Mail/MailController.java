package com.hallym.project.RingRingRing.Mail;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MailController {
	private final MailService mailService;
	
	@GetMapping("/mailsender/{email}")
	public String MailSend(@PathVariable("email") String email) {
		mailService.sendMail(email);
		return "메일을 보냈습니다.";
	}
}
