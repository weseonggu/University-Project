package com.hallym.project.RingRingRing.error;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hallym.project.RingRingRing.customexception.IDOverlapException;
import com.hallym.project.RingRingRing.customexception.UserNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	/**
	 * UsernameNotFoundException 로그인 loadUserByUsername메소드 실행 중 발생하는 예외 처리
	 * @param ex
	 * @param request
	 * @return ErrorDetails 객체와 상채 코드가 있는 ResponseEntity가 리턴
	 * @throws Exception
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handeleUsernameNotFoundException(Exception ex, WebRequest request) throws Exception{
		ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	/**
	 * 가입 요청시 아미 아이디가 있으면 발생하는 요청
	 * @param ex
	 * @param request
	 * @return ErrorDetails 객체와 상채 코드가 있는 ResponseEntity가 리턴
	 * @throws Exception
	 */
	@ExceptionHandler(IDOverlapException.class)
	public final ResponseEntity<ErrorDetails> handleSQLIntegrityConstraintViolationException(Exception ex, WebRequest request)throws Exception{
		ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.CONFLICT);
	}
	

	
	/**
	 * 유효성 검증실패시 예외처리 오버라이드
	 * @return Example: {"timeStamp": "2024-04-14","message": "Validation failed","details": "[8자 이상글자에 적어도 하나의 특수문자, 영문자, 숫자를 포함해야 합니다.]"}
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
		    
		ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(),"Validation failed",errorMessages.toString());
		return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
	}
	/**
	 * 토큰 SignatureException
	 * @return ErrorDetails 객체
	 */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorDetails> handleSignatureException(Exception ex, WebRequest request) {
    	ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), "JWT가 변조되었습니다.", request.getDescription(false));
    	return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
	/**
	 * 토큰  MalformedJwtException
	 * @return ErrorDetails 객체
	 */
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorDetails> handleMalformedJwtException(Exception ex, WebRequest request) {
    	ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), "JWT가 형식에 맞지 않거나 손상되었습니다.", request.getDescription(false));
    	return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
	/**
	 * 토큰 변조 ExpiredJwtException
	 * @return ErrorDetails 객체
	 */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorDetails> handleExpiredJwtException(Exception ex, WebRequest request) {
    	ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), "기간이 만료되었습니다.", request.getDescription(false));
    	return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}
