package com.shen.concurrent;

//如果不抛出异常不走异常处理中心
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("捕获到异常：" + e);
	}
}
