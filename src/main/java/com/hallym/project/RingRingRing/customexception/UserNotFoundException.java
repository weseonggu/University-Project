package com.hallym.project.RingRingRing.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends AuthenticationException{
	public UserNotFoundException(String msg) {
        super(msg);
    }

}
