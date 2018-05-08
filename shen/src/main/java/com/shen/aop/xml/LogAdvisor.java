package com.shen.aop.xml;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

//实现接口MethodBeforeAdvice该拦截器会在调用方法前执行
//实现接口   AfterReturningAdvice该拦截器会在调用方法后执行
//实现接口  MethodInterceptor该拦截器会在调用方法前后都执行，实现环绕结果。
public class LogAdvisor implements MethodBeforeAdvice {

	public void before(Method method, Object[] args, Object target) throws Throwable {

		System.out.println("我要打印日志喽！ [log] " + target.getClass().getName() + "." + method.getName() + "( )");
	}

	// @Override
	// public void before(Method arg0, Object[] arg1, Object arg2) throws
	// Throwable {
	// saveBeforeMessage();
	// }
	//
	// @Override
	// public void afterReturning(Object arg0, Method arg1, Object[] arg2,
	// Object arg3) throws Throwable {
	// saveAfterMessage();
	// }
	//
	// public void saveBeforeMessage() {
	// System.out.println("调用BeforeAdvice成功");
	// }
	//
	// public void saveAfterMessage() {
	// System.out.println("调用AfterAdvice成功");
	// }
	//
	// @Override
	// public Object invoke(MethodInvocation arg0) throws Throwable {
	// System.out.println("调用RoundService之前成功");
	// Object result = arg0.proceed();
	// System.out.println("调用RoundService之后成功");
	// return result;
	// }

}
