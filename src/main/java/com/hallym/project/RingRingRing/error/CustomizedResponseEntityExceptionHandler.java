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

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handeleUsernameNotFoundException(Exception ex, WebRequest request) throws Exception{
		ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(IDOverlapException.class)
	public final ResponseEntity<ErrorDetails> handleSQLIntegrityConstraintViolationException(Exception ex, WebRequest request)throws Exception{
		ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.CONFLICT);
	}
	
	//유형 검사예외처리
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

//	@ExceptionHandler(TokenPeriodException.class)
//	public final ResponseEntity<ErrorDetails> handleTokenPeriodException(Exception ex, WebRequest request)throws Exception{
//		ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
//		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.FORBIDDEN);
//	}
//	
//	@ExceptionHandler(NoAuthenticationException.class)
//	public final ResponseEntity<ErrorDetails> handleNoAuthenticationException(Exception ex, WebRequest request)throws Exception{
//		ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
//		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNAUTHORIZED);
//	}
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorDetails> handleSignatureException(Exception ex, WebRequest request) {
    	ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), "JWT가 변조되었습니다.", request.getDescription(false));
    	return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNAUTHORIZED);
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 유효하지 않습니다."));
    }
 
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorDetails> handleMalformedJwtException(Exception ex, WebRequest request) {
    	ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), "JWT가 형식에 맞지 않거나 손상되었습니다.", request.getDescription(false));
    	return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNAUTHORIZED);
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("올바르지 않은 토큰입니다."));
    }
 
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorDetails> handleExpiredJwtException(Exception ex, WebRequest request) {
    	ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), "기간이 만료되었습니다.", request.getDescription(false));
    	return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNAUTHORIZED);
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 만료되었습니다. 다시 로그인해주세요."));
    }
}
