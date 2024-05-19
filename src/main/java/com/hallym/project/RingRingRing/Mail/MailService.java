package com.hallym.project.RingRingRing.Mail;

import java.time.LocalDateTime;

import org.springframework.data.domain.Sort;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hallym.project.RingRingRing.Mail.DTO.MailCodeDTO;
import com.hallym.project.RingRingRing.Mail.entity.EmailAuthenticationEntity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
	
	private static final int EXPIRATION_MINUTES = 10;
	
	private final JavaMailSender javaMailSender;
	private static final String senderEmail = "ringringringproject@gmail.com";
	private final MailRepository mailRepository;
	
	
	/*
	 * 코드 생성
	 */
    public static int createNumber(){
        return (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }
    /*
     * 메일 내용 작성
     */
    public MimeMessage CreateMail(String mail, int number){
        
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            StringBuffer body = new StringBuffer();
            body.append("<h3>" + "요청하신 인증 번호입니다." + "</h3>");
            body.append("<h1>" + number + "</h1>");
            body.append("<h3>" + "감사합니다." + "</h3>");
            message.setText(body.toString(),"UTF-8", "html");
        } catch (MessagingException e) {
            log.warn(e.getMessage());
            throw new MailSendException("인증 코드 전송 실패");
        }

        
        return message;
    }

    /*
     * 인증코드 이메일 전송
     * 메일 전송 및 코드 DB에 저장
     */
    public void sendMail(String mail){
    	int number = createNumber();
        MimeMessage message = CreateMail(mail, number);
        try {
        	// 데이터 저잗
        	EmailAuthenticationEntity data = EmailAuthenticationEntity.builder()
        			.email(mail)
        			.timeStamp(LocalDateTime.now())
        			.code(number)
        			.build();
        	
        	javaMailSender.send(message);
        	
        	mailRepository.save(data);
        	log.info("인증 코드 전송: "+mail);
        }catch (Exception e) {
        	// 이메일 전송 실패 전송 커스텀 예외 처리 하기
        	log.warn(mail+"로 인증 코드 전송 실패");
        	throw new MailSendException("인증 코드 전송 실패");
        	
        	
		}
    }
    
    /*
     * 인증 코드 검증
     */
    public boolean codeVerification(MailCodeDTO data) {

    	EmailAuthenticationEntity codeNemail = mailRepository.findByEmail(data.getEmail(), Sort.by(Sort.Direction.DESC, "id")).get(0);
    	
    	LocalDateTime currentTime = LocalDateTime.now();
    	
    	LocalDateTime expirationTime = codeNemail.getTimeStamp().plusMinutes(EXPIRATION_MINUTES);
    	
    	return currentTime.isBefore(expirationTime) && data.getCode() == codeNemail.getCode();
    	
    }

}
