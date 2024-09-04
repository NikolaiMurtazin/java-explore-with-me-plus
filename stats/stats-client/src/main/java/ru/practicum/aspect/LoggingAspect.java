package ru.practicum.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
//
//    @Before("execution(* ru.practicum.controller.*.*(..))")
//    public void logBefore(JoinPoint joinPoint) {
//        log.info("Entering in Method :  {}", joinPoint.getSignature().getName());
//        log.info("Class Name :  {}", joinPoint.getSignature().getDeclaringTypeName());
//        log.info("Arguments :  {}", Arrays.toString(joinPoint.getArgs()));
//    }
//
//    @AfterReturning(pointcut = "execution(* ru.practicum.controller.*.*(..))", returning = "result")
//    public void logAfter(JoinPoint joinPoint, Object result) {
//        log.info("Method Return value : {}", result);
//        log.info("Exiting from Method :  {}", joinPoint.getSignature().getName());
//    }
}
