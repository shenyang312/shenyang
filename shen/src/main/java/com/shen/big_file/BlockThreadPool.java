package com.shen.big_file;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义阻塞型线程池 当池满时会阻塞任务提交
 *
 * @ClassName: BlockThreadPool
 * @Description: 通过将队列的offer改为put方法（阻塞），把TreadPool改造成阻塞队列，任务超过线程数量将会阻塞
 * @author: chujiejie
 * @date: 2018-7-19 下午5:24:54
 */
public class BlockThreadPool {

    private ThreadPoolExecutor pool = null;

    public BlockThreadPool(int poolSize) {
        pool = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(poolSize),
                new CustomThreadFactory(), new CustomRejectedExecutionHandler());
    }

    /**
     *
     * @author chujiejie
     *
     */
    private class CustomThreadFactory implements ThreadFactory {
        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName = BlockThreadPool.class.getSimpleName() + count.addAndGet(1);
            t.setName(threadName);
            return t;
        }
    }

    /**
     * 改造任务入队方法
     * @author chujiejie
     *
     */
    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                // 核心改造，由blockingqueue的offer改成put阻塞方法
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 包装execte方法
     * @param runnable
     */
    public void execute(Runnable runnable) {
        this.pool.execute(runnable);
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        this.pool.shutdown();
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        BlockThreadPool pool = new BlockThreadPool(3);
        for (int i = 1; i < 100; i++) {
            System.out.println("提交第" + i + "个任务");
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getId() + "=====开始");
                        TimeUnit.SECONDS.sleep(10);
                        System.out.println(Thread.currentThread().getId() + "=====结束");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("提交第" + i + "个任务成功");
        }
    }


}
