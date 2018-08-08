package com.shen.jmm.threadPool;

import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public static void main(String[] args) {
        ThreadPool pool = new DefaultThreadPool();
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
        pool.execute(new NewJob());
    }
}
class NewJob implements Job{

    @Override
    public void run() {
        try {
            System.out.println("开始搬砖");
            TimeUnit.SECONDS.sleep(10);
            System.out.println("搬砖完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


