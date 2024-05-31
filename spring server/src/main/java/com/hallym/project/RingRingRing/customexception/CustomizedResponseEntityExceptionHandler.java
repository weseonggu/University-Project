package com.hallym.project.RingRingRing.customexception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hallym.project.RingRingRing.message.CurrentTime;
import com.hallym.project.RingRingRing.message.FailMessage;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	private final CurrentTime rTime;
	
	/**
	 * UsernameNotFoundException 로그인 loadUserByUsername메소드 실행 중 발생하는 예외 처리
	 * @param ex
	 * @param request
	 * @return ErrorDetails 객체와 상채 코드가 있는 ResponseEntity가 리턴
	 * @throws Exception
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<FailMessage> handeleUsernameNotFoundException(Exception ex, WebRequest request) throws Exception{
		FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
		return new ResponseEntity<FailMessage>(message,HttpStatus.NOT_FOUND);
	}
	

	

	
	@ExceptionHandler(MailSendFailException.class)
	public final ResponseEntity<FailMessage> handleMailSendFailException(Exception ex, WebRequest request)throws Exception{
		FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of("인증 코드전송 실패"));
		return new ResponseEntity<FailMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

	
	/**
	 * 유효성 검증실패시 예외처리 오버라이드
	 * @return Example: {"timeStamp": "yyyy-MM-dd HH:mm:ss","endPoint": "uri=/signup","errorDetails":["이름을 입력해주세요", "이메일을 입력해주세요", "이메일 형식으로 입력해 주세요", "8자 이상글자에 적어도 하나의 특수문자, 영문자, 숫자를 포함해야 합니다."]}
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		   BindingResult bindingResult = ex.getBindingResult();
		    List<FieldError> fieldErrors = bindingResult.getFieldErrors();

		    List<String> errorMessages = new ArrayList<>();
		    for (FieldError fieldError : fieldErrors) {
		        errorMessages.add(fieldError.getDefaultMessage());
		    }
		    
		FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), errorMessages);
		return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
	}
	/**
	 * 토큰 SignatureException
	 * @return ErrorDetails 객체
	 */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<FailMessage> handleSignatureException(Exception ex, WebRequest request) {
		FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of("JWT가 변조되었습니다."));
    	return new ResponseEntity<FailMessage>(message, HttpStatus.UNAUTHORIZED);
    }
	/**
	 * 토큰  MalformedJwtException
	 * @return ErrorDetails 객체
	 */
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<FailMessage> handleMalformedJwtException(Exception ex, WebRequest request) {
		FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of("JWT가 형식에 맞지 않거나 손상되었습니다."));
    	return new ResponseEntity<FailMessage>(message, HttpStatus.UNAUTHORIZED);
    }
	/**
	 * 토큰 변조 ExpiredJwtException
	 * @return ErrorDetails 객체
	 */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<FailMessage> handleExpiredJwtException(Exception ex, WebRequest request) {
		FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of("토큰 기간이 만료되었습니다."));
    	return new ResponseEntity<FailMessage>(message, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<FailMessage> handleRuntimeException(Exception ex, WebRequest request) {
    	FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
		return new ResponseEntity<FailMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<FailMessage> handleDataAccessException(Exception ex, WebRequest request) {
    	FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
		return new ResponseEntity<FailMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<FailMessage> handleAuthenticationServiceException(Exception ex, WebRequest request) {
    	FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
		return new ResponseEntity<FailMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
