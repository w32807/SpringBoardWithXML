package com.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Aspect
@Log4j
@Component // AOP Ŭ������ Bean���� �ν��ϵ��� ����
public class LogAdvice {
	
	// AspectJ�� ǥ������ ��� (���� �н� �� �ʿ�)
	// 1. � ��ġ�� Advice�� ������ ������ ����. 
	@Before("execution(* com.zerock.service.SampleService*.*(..))")
	public void logBefore() {
		log.info("==============");
	}
	
	// Ư�� �޼ҵ带 �����Ͽ� Advice�� �����Ŵ
	@Before("execution(* com.zerock.service.SampleService*.doAdd(String, String))"
			+ "&& args(str1, str2)")
	public void logBeforeWithParam(String str1, String str2) {
		log.info("str1 >> " + str1);
		log.info("str2 >> " + str2);
	}
	
	@AfterThrowing(pointcut = "execution(* com.zerock.service.SampleService*.*(..))"
			,throwing = "exception")
	public void logException(Exception exception) {
		log.info("Exception.....");
		log.info("Exception >> " + exception);
	}
	
	@Around("execution(* com.zerock.service.SampleService*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) {
		long start = System.currentTimeMillis();
		log.info("Target >> " + pjp.getTarget());
		log.info("Param >> " + Arrays.toString(pjp.getArgs()));
		
		Object result = null;
		
		try {
			result = pjp.proceed();
		}catch (Throwable e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		log.info("TIME >> " + (end - start));
		
		return result;
	}
}
