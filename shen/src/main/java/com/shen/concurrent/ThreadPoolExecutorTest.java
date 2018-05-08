package com.shen.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 入参:
// int corePoolSize 核心池的大小
// int maximumPoolSize,线程池最大线程数
// long keepAliveTime,表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，
// 直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，
// 直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，
// keepAliveTime参数也会起作用，直到线程池中的线程数为0
// TimeUnit unit,参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性：
// TimeUnit.DAYS; //天
// TimeUnit.HOURS; //小时
// TimeUnit.MINUTES; //分钟
// TimeUnit.SECONDS; //秒
// TimeUnit.MILLISECONDS; //毫秒
// TimeUnit.MICROSECONDS; //微妙
// TimeUnit.NANOSECONDS; //纳秒
// BlockingQueue<Runnable> workQueue
// 一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择
// ArrayBlockingQueue;
// LinkedBlockingQueue;
// SynchronousQueue;
//1）ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小；
//2）LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；
//3）synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。
public class ThreadPoolExecutorTest {
	// 从执行结果可以看出，当线程池中线程的数目大于5时，便将任务放入任务缓存队列里面，
	// 当任务缓存队列满了之后，便创建新的线程。如果上面程序中，将for循环中改成执行20个任务，就会抛出任务拒绝异常了。
	// 一般需要根据任务的类型来配置线程池大小：
	// 最佳线程数目 = l（线程等待时间与线程CPU时间之比 + 1）* CPU数目
	// 如果是CPU密集型任务，就需要尽量压榨CPU，参考值可以设为 NCPU+1
	// 如果是IO密集型任务，参考值可以设置为2*NCPU
	public static void main(String[] args) {
		// 核心线程数，最大线程数，线程最多时间，单位，阻塞队列类型
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(5), new HandlerThreadFactory());

		for (int i = 0; i < 15; i++) {
			MyTask myTask = new MyTask(i);
			executor.execute(myTask);
			System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" + executor.getQueue().size()
					+ "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
		}
		// shutdown()：不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务
		// shutdownNow()：立即终止线程池，并尝试打断正在执行的任务，并且清空任务缓存队列，返回尚未执行的任务
		// 当线程池处于SHUTDOWN或STOP状态，并且所有工作线程已经销毁，任务缓存队列已经清空或执行结束后，线程池被设置为TERMINATED状态
		executor.shutdown();
	}
}

class MyTask implements Runnable {
	private int taskNum;

	public MyTask(int num) {
		this.taskNum = num;
	}

	@Override
	public void run() {
		System.out.println("正在执行task " + taskNum);
		try {
			Thread.currentThread().sleep(taskNum*4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("task " + taskNum + "执行完毕");
	}

}
