package com.shen.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SystemLogAspect {


	private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

	// Controller层切点
	@Pointcut("execution (* com.shen.service..*.*(..))")
	public void controllerAspect() {
		System.out.println("1111111111111");
	}

	/**
	 * 前置通知 用于拦截Controller层记录用户的操作
	 * 
	 * @param joinPoint
	 *            切点
	 */
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) {
		System.out.println("==========执行controller前置通知===============");
		if (logger.isInfoEnabled()) {
			logger.info("before " + joinPoint);
		}
	}

	// 配置controller环绕通知,使用在方法aspect()上注册的切入点
	@Around("controllerAspect()")
	public void around(JoinPoint joinPoint) {
		System.out.println("==========开始执行controller环绕通知===============");
		long start = System.currentTimeMillis();
	}

	/**
	 * 后置通知 用于拦截Controller层记录用户的操作
	 * 
	 * @param joinPoint
	 *            切点
	 */
	@After("controllerAspect()")
	public void after(JoinPoint joinPoint) {

		/*
		 * HttpServletRequest request = ((ServletRequestAttributes)
		 * RequestContextHolder.getRequestAttributes()).getRequest();
		 * HttpSession session = request.getSession();
		 */
		// 读取session中的用户
		// User user = (User) session.getAttribute("user");
		// 请求的IP
		// String ip = request.getRemoteAddr();
	}

	// 配置后置返回通知,使用在方法aspect()上注册的切入点
	@AfterReturning("controllerAspect()")
	public void afterReturn(JoinPoint joinPoint) {
		System.out.println("=====执行controller后置返回通知=====");
		if (logger.isInfoEnabled()) {
			logger.info("afterReturn " + joinPoint);
		}
	}

	/**
	 * 异常通知 用于拦截记录异常日志
	 * 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		/*
		 * HttpServletRequest request = ((ServletRequestAttributes)
		 * RequestContextHolder.getRequestAttributes()).getRequest();
		 * HttpSession session = request.getSession(); //读取session中的用户 User user
		 * = (User) session.getAttribute(WebConstants.CURRENT_USER); //获取请求ip
		 * String ip = request.getRemoteAddr();
		 */
		// 获取用户请求方法的参数并序列化为JSON格式字符串

	}

}