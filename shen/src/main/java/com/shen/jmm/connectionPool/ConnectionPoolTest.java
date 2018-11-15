package com.shen.jmm.connectionPool;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接池测试代码
 */
public class ConnectionPoolTest {
    static ConnectionPool pool = new ConnectionPool(10);
    //保证所有ConnectionRunner能够同事开始
    static CountDownLatch start = new CountDownLatch(1);
    //main线程将会等待所有ConnectionRunner结束后才能继续执行
    static CountDownLatch end;

    public static void main(String[] args) throws Exception {
        //线程数量，可以修改线程数量进行观察
        int threadCount = 20;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnetionRunner(count, got, notGot), "ConnectionRunnerThread");
            thread.start();
        }
        //所有线程都循环执行完毕，countDown 方法 对start进行-1，当计数器为0，唤醒全部的await()方法
        start.countDown();
        //还未全部执行完成，即对主线程进行中断，每执行一个 end 的 监控数量  -1
        end.await();
        //总共有多少个连接需求
        System.out.println("total invoke :" + (threadCount * count));
        //成功连接数量
        System.out.println("got connection: " + got);
        //未连接数量
        System.out.println("not got connection " + notGot);
    }

    static class ConnetionRunner implements Runnable {
        //count 获取多少个链接
        int count;
        //已获取连接数量
        AtomicInteger got;
        //未获得连接数量
        AtomicInteger notGot;

        public ConnetionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        public void run() {
            try {
                //线程中断，等全部线程执行完成，被唤醒
                start.await();
            } catch (Exception ex) {

            }
            while (count > 0) {
                try {
                    //从线程池中获取连接，如果1000ms内无法获取到，将返回null
                    //分别统计连接获取的数量got和为获取到的数量notGot
                    //如果不设置超时时间，连接池代码中的逻辑是一直等待，直到有可用连接
                    Connection connection = pool.fetchConnection(1000);
                    if (connection != null) {
                        try {
                            //创建语句
                            connection.createStatement();
                            //提交
                            connection.commit();
                        } finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    } else {
                        notGot.incrementAndGet();
                    }
                } catch (Exception ex) {

                } finally {
                    count--;
                }
            }
            //线程执行完成，对线程计数器-1
            end.countDown();
        }
    }
}
