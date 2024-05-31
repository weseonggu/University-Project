package com.hallym.project.RingRingRing.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspectOfLogin {

 // 요청
    @Before("execution(* com.hallym.project.RingRingRing.Login.LoginController.*(..))")
    public void logControllerBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            log.info("[Login Request]: ip: {} Resources Accessed: {}", attributes.getRequest().getRemoteAddr(),joinPoint.getSignature());
        }
        else {
        	
        	log.info("[Login Request]: {}",joinPoint.getSignature());
        }
    }
    // 프로제세스 시작
    @Before("execution(* com.hallym.project.RingRingRing.Login.LoginService.loginService(..))")
    public void logServiceBefore(JoinPoint joinPoint) {
        log.info("[Processing Start]: {} ", joinPoint.getSignature());
    }

    
    // 응답
    @After("execution(* com.hallym.project.RingRingRing.Login.LoginController.*(..))")
    public void logControllerAfter(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            log.info("[Login Response]: {} {} ", attributes.getRequest().getRemoteAddr(),joinPoint.getSignature());
        }else {
        	
        	log.info("[Login Response]: {}",joinPoint.getSignature());
        }
    }
    // 프로세스 끝
    @After("execution(* com.hallym.project.RingRingRing.Login.LoginService.loginService(..))")
    public void logServiceAfter(JoinPoint joinPoint) {
        log.info("[Processing End]: {} ", joinPoint.getSignature());
    }

    
    
    // 응답 정상
    @AfterReturning("execution(* com.hallym.project.RingRingRing.Login.LoginController.*(..))")
    public void logControllerAfterReturning(JoinPoint joinPoint) {
        log.info("[Normal Response]: {} ", joinPoint.getSignature());
    }
    //프로세스 정상
    @After("execution(* com.hallym.project.RingRingRing.Login.LoginService.loginService(..))")
    public void logServiceAfterReturning(JoinPoint joinPoint) {
        log.info("[Normal Processing End]: {} ", joinPoint.getSignature());
    }

    
    
    
    
    
    
    
    @AfterThrowing(pointcut = "execution(* com.hallym.project.RingRingRing.Login.LoginService.loginService(..))", throwing = "exception")
    public void logServiceAfterThrowing(RuntimeException exception) {
        log.error("[Exception caught]: ", exception);
    }
    

}
