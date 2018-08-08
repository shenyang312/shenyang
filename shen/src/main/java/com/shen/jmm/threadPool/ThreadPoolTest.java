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
            System.out.println("睡觉中");
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


