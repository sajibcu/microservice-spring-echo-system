package com.learn.salesservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class ExecutionTimeLoggerAspect {

    Logger log = Logger.getLogger(ExecutionTimeLoggerAspect.class.getName());

    @Around("execution(* com.learn.salesservice..*(..)) && !within(com.learn.salesservice.aop..*)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info("ClassName " + className + " Method " + methodName + " executed in " + executionTime + "ms");

        return proceed;
    }
}
