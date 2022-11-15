package com.cognizant.aoppoc.aspects;

import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j2
public class MyAopAspect {

    @Pointcut("execution(* com.cognizant..*Service.*(..))")
    public void executeService(){};

    @Pointcut("execution(* com.cognizant..*Controller.*(..))")
    public void executeController(){};

    //@Before("executeService() && executeController()")
    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        log.info("*****************************************");
        log.info("[" + joinPoint.getSignature().getName() + "]- "
                + "Before advice");
    }

    @AfterReturning(value = "executeService()", returning = "keys")
    public void doAfterReturning(JoinPoint joinPoint, Object keys){
        log.info("[" + joinPoint.getSignature().getName() + "]- " +
                "Return-value:" + keys);
        log.info("*****************************************");
    }

    @AfterThrowing(value = "executeService()", throwing = "exception")
    public void doAfterException(JoinPoint joinPoint, Throwable exception){
        log.info(joinPoint.getSignature().getName());
        if(exception instanceof RuntimeException){
            log.info("[" + joinPoint.getSignature().getName() + "]- " +
                    "A null pointer exception occurred!!!");
        }
    }

}
