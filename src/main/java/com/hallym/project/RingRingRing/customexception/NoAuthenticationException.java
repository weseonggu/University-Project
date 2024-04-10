package com.hallym.project.RingRingRing.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class NoAuthenticationException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public NoAuthenticationException(String message) {
		super(message);
	}

}
