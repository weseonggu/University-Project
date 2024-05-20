package com.hallym.project.RingRingRing.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UsageFailException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UsageFailException(String message){
        super(message);
    }
}
