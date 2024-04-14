package com.hallym.project.RingRingRing.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * 회원 가입 요청시 이미 가입자가 있는 경우 발생하는 예외
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class IDOverlapException extends RuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IDOverlapException(String message) {
		super(message);
	}
	
	
}
