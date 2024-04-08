package com.hallym.project.RingRingRing.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class TokenPeriodException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public TokenPeriodException(String message) {
		super(message);
	}
}
