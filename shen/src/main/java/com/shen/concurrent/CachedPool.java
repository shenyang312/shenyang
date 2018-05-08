package com.shen.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedPool {
	public static void main(String[] args) {
		// 线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new HandlerThreadFactory());
		// for (int i = 0; i < 10; i++) {
		// final int index = i;
		// try {
		// // Unix系统使用的是时间片算法，而Windows则属于抢占式的。
		// // Thread.Sleep(0)的作用，就是“触发操作系统立刻重新进行一次CPU竞争”。
		//// Thread.sleep(index * 1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		cachedThreadPool.execute(new Runnable() {
			public void run() {
				// System.out.println(index);
				Thread t = Thread.currentThread();
				System.out.println("run() by " + t);
				System.out.println("eh=" + t.getUncaughtExceptionHandler());
				// throw new RuntimeException();
			}
		});
		// }
	}
}
