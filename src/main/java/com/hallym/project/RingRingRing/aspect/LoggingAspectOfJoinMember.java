package com.hallym.project.RingRingRing.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspectOfJoinMember {
	// 요청
    @Before("execution(* com.hallym.project.RingRingRing.joinmember.JoinMembershipController.*(..))")
    public void logControllerBefore(JoinPoint joinPoint) {
        log.info("[Request]: {} ", joinPoint.getSignature());
    }
    // 프로제세스 시작
    @Before("execution(* com.hallym.project.RingRingRing.joinmember.JoinMembershipService.*(..))")
    public void logServiceBefore(JoinPoint joinPoint) {
        log.info("[Processing Start]: {} ", joinPoint.getSignature());
    }
    // DB 트렌젝션 시작
    @Before("execution(* com.hallym.project.RingRingRing.joinmember.repository.UserRepository.*(..))")
    public void logRepositoryBefore(JoinPoint joinPoint) {
        log.info("[DB Transaction Start]: {} ", joinPoint.getSignature());
    }
    
    // 응답
    @After("execution(* com.hallym.project.RingRingRing.joinmember.JoinMembershipController.*(..))")
    public void logControllerAfter(JoinPoint joinPoint) {
        log.info("[Response]: {} ", joinPoint.getSignature());
    }
    // 프로세스 끝
    @After("execution(* com.hallym.project.RingRingRing.joinmember.JoinMembershipService.*(..))")
    public void logServiceAfter(JoinPoint joinPoint) {
        log.info("[Processing End]: {} ", joinPoint.getSignature());
    }
    // 트렌잭션 끝
    @After("execution(* com.hallym.project.RingRingRing.joinmember.repository.UserRepository.*(..))")
    public void logRepositoryAfter(JoinPoint joinPoint) {
        log.info("[DB Transaction End]: {} ", joinPoint.getSignature());
    }
    
    
    // 응답 정상
    @AfterReturning("execution(* com.hallym.project.RingRingRing.joinmember.JoinMembershipController.*(..))")
    public void logControllerAfterReturning(JoinPoint joinPoint) {
        log.info("[Normal Response]: {} ", joinPoint.getSignature());
    }
    //프로세스 정상
    @After("execution(* com.hallym.project.RingRingRing.joinmember.JoinMembershipService.*(..))")
    public void logServiceAfterReturning(JoinPoint joinPoint) {
        log.info("[Normal Processing End]: {} ", joinPoint.getSignature());
    }
    // 트렌잭션 끝 정상
    @AfterReturning("execution(* com.hallym.project.RingRingRing.joinmember.repository.UserRepository.*(..))")
    public void logRepositoryAfterReturning(JoinPoint joinPoint) {
    	log.info("[Normal DB Transaction End]: {} ", joinPoint.getSignature());
    }
    
    
    
    
    
    
    
    @AfterThrowing(pointcut = "execution(* com.hallym.project.RingRingRing.joinmember.JoinMembershipService.*(..))", throwing = "exception")
    public void logServiceAfterThrowing(RuntimeException exception) {
        log.error("[Exception caught]: ", exception);
    }
    
    @AfterThrowing(pointcut = "execution(* com.hallym.project.RingRingRing.joinmember.repository.UserRepository.*(..))", throwing = "exception")
    public void logRepositoryAfterThrowing(RuntimeException exception) {
        log.error("[Exception caught]: ", exception);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

}