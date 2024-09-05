package ru.practicum.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* ru.practicum.admin.controller.*.*(..)) || " +
            "execution(* ru.practicum.category.controller.*.*(..)) || " +
            "execution(* ru.practicum.event.controller.*.*(..)) || " +
            "execution(* ru.practicum.user.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering in Method :  {}", joinPoint.getSignature().getName());
        log.info("Class Name :  {}", joinPoint.getSignature().getDeclaringTypeName());
        log.info("Arguments :  {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* ru.practicum.admin.controller.*.*(..)) || " +
            "execution(* ru.practicum.category.controller.*.*(..)) || " +
            "execution(* ru.practicum.event.controller.*.*(..)) || " +
            "execution(* ru.practicum.user.controller.*.*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("Method Return value : {}", result);
        log.info("Exiting from Method :  {}", joinPoint.getSignature().getName());
    }
}
