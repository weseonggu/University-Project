package com.hallym.project.RingRingRing.Mail;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hallym.project.RingRingRing.DTO.MailCodeDTO;
import com.hallym.project.RingRingRing.repository.MailRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="이메일 인증 컨트롤러", description = "이메일로 인증관련 컨트롤러")
public class MailController {
	private final MailService mailService;
	private final MailRepository mailRepository;
	
	@GetMapping("/mailsender/{email}")
	@Operation(summary = "이메일 인증코드 전송 api", description = "메일 전송까지 시간이 걸려서 응답이 쫌 늦게 옵니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "코드전송 성공"),
			@ApiResponse(responseCode = "500", description = "코드전송 실패")
	})
	public ResponseEntity<String> MailSend(@PathVariable("email") String email) {
		log.info("이메일 인증: "+email);
		mailService.sendMail(email);
		return new ResponseEntity<String>("인증 메일을 보냈습니다.", HttpStatus.OK);
	}
	
	@PostMapping("/codecheck")
	@Operation(summary = "인증코드 검증 api", description = "검증 완료 후 회원가입 버튼 활성화 하도록 부탁드립니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "코드인증 성공"),
			@ApiResponse(responseCode = "400", description = "코드인증 실패")
	})
	public ResponseEntity<String> codeCheck(@RequestBody MailCodeDTO mailCode) {
		
		if(mailService.codeVerification(mailCode)) {
			mailRepository.deleteByEmail(mailCode.getEmail());
			return new ResponseEntity<String>("메일 인증 성공", HttpStatus.OK);
		}
		else {	
			return new ResponseEntity<String>("인증코드가 틀립니다.", HttpStatus.BAD_REQUEST);
		}
		
	}
}
