package com.shen.concurrent;

import java.util.concurrent.ThreadFactory;

class HandlerThreadFactory implements ThreadFactory {
	@Override
	public Thread newThread(Runnable r) {
		System.out.println("创建一个新的线程");
		Thread t = new Thread(r);
		System.out.println("created " + t + " ID:" + t.getId());
		t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		System.out.println("eh121=" + t.getUncaughtExceptionHandler());
		return t;
	}
}
