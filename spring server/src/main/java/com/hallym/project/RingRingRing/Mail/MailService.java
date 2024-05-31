package com.hallym.project.RingRingRing.Mail;

import java.time.LocalDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hallym.project.RingRingRing.Mail.DTO.MailCodeDTO;
import com.hallym.project.RingRingRing.Mail.entity.EmailAuthenticationEntity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@Transactional(readOnly = true)
public class MailService {
	
	private static final int EXPIRATION_MINUTES = 10;
	
	private final JavaMailSender javaMailSender;
	private static final String senderEmail = "ringringringproject@gmail.com";
	private final MailRepository mailRepository;
	
	

	/**
	 * 코그생성
	 * @return int 코드
	 */
    public static int createNumber(){
        return (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }
    /**
     * 이메일 작성 method
     * @param mail 보낼 메일
     * @param number 랜덤으로 생성된 코드
     * @return MimeMessage 메일 내용
     */
    private MimeMessage createMail(String mail, int number)throws MessagingException{
        
        MimeMessage message = javaMailSender.createMimeMessage();


        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("이메일 인증");
        StringBuffer body = new StringBuffer();
        body.append("<h3>" + "요청하신 인증 번호입니다." + "</h3>");
        body.append("<h1>" + number + "</h1>");
        body.append("<h3>" + "감사합니다." + "</h3>");
        message.setText(body.toString(),"UTF-8", "html");


        
        return message;
    }

    /**
     * 메일 보네기 
     * @param mail 보낼 상대의 메일주소
     * @throws MailException, RuntimeException
     */
    @Transactional(rollbackFor = { RuntimeException.class, DataAccessException.class, MailException.class })
    public void sendMail(String mail){
        try {
        	int number = createNumber();
        	MimeMessage message = createMail(mail, number);
        	// 데이터 저잗
        	EmailAuthenticationEntity data = EmailAuthenticationEntity.builder()
        			.email(mail)
        			.timeStamp(LocalDateTime.now())
        			.code(number)
        			.isCheckCode(false)
        			.build();
        	
        	javaMailSender.send(message);
        	
        	mailRepository.save(data);
        	log.info("인증 코드 전송: "+mail);
        }catch (MailException e) {
        	throw new MailSendException("인증 코드 전송 실패");
		}catch (DataAccessException e) {
			throw new RuntimeException(e);
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}catch (Exception e) {
		}
    }
    

    /**
     * 이메일 코드 검증
     * @param data
     * @return
     */
    @Transactional(rollbackFor = { RuntimeException.class, DataAccessException.class})
    public boolean codeVerification(MailCodeDTO data) {
    	try {
    	EmailAuthenticationEntity codeNemail = mailRepository.findByEmail(data.getEmail(), Sort.by(Sort.Direction.DESC, "id")).get(0);
    	
    	LocalDateTime currentTime = LocalDateTime.now();
    	
    	LocalDateTime expirationTime = codeNemail.getTimeStamp().plusMinutes(EXPIRATION_MINUTES);
    	
    	if(currentTime.isBefore(expirationTime) && data.getCode() == codeNemail.getCode()) {
    		
    		codeNemail = codeNemail.toBuilder()
    				.isCheckCode(true)
    				.build();
    		
    		
    		mailRepository.save(codeNemail);
    		
    		return true;
    	}
    	else {
    		return false;
    	}
		}catch (IndexOutOfBoundsException e) {
			return false;
		}catch (DataAccessException e) {
			throw new RuntimeException(e);
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}catch (Exception e) {
			throw e;
		}
    	
    }
    
    
	@Transactional(rollbackFor = { RuntimeException.class, DataAccessException.class })
	@Scheduled(cron = "0 0 0 * * *")
	public void cleanupOldData() {
		try {
			LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
			mailRepository.deleteOlderThanTenMinutesEmailCode(tenMinutesAgo);

		} catch (DataAccessException e) {
			throw new RuntimeException(e);
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

}
