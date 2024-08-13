package com.example.systemtaskmanagement.aop;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Log


public class LoggingAspect {


    @Around("execution(* com.example.systemtaskmanagement.controller..*(..)))")
    public Object profileControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {


        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();


        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        log.info("-------- Executing " + className + "." + methodName + "   ----------- " +
                LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + " | | " + LocalDate.now());

        StopWatch countdown = new StopWatch();


        countdown.start();
        Object result = proceedingJoinPoint.proceed(); // выполняем сам метод
        countdown.stop();

        log.info("-------- Execution time of " + className + "." + methodName + " :: " + countdown.getTotalTimeMillis() + " ms");

        return result;
    }

}
