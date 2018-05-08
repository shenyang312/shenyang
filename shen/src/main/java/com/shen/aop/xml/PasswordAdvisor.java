package com.shen.aop.xml;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class PasswordAdvisor implements MethodInterceptor {

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object ret = invocation.proceed();
		if (ret == null)
			return null;
		String password = (String) ret;
		StringBuffer alter = new StringBuffer(password.length());
		for (int i = 0; i < password.length(); i++) {
			alter.append("*");
		}

		return alter.toString();
	}

}