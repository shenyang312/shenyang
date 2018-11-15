package com.shen.jmm.connectionPool;

import java.sql.Connection;
import java.util.LinkedList;

public class ConnectionPool {
    //注意一点，不管是获取 释放 都会对线程池加锁
    private LinkedList<Connection> pool = new LinkedList<Connection>();

    /**
     * 创建连接池
     *
     * @param initialSize（连接池大小）
     */
    public ConnectionPool(int initialSize) {
        if (initialSize > 0) {
            for (int i = 0; i < initialSize; i++) {
                //总是加在最后
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }

    /**
     * 释放连接
     *
     * @param connection（归还连接）
     */
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (pool) {
                //连接释放后需要进行通知，这样其他消费者能够感知到连接池中已经归还了一个链接
                pool.addLast(connection);//每次都加到最后
                pool.notifyAll();//有归还线程，通知等待线程（等待队列）
            }
        }
    }

    /**
     * 获取线程
     *
     * @param mills 内无法获取到连接，将会返回null(毫秒)
     * @return 连接
     * @throws InterruptedException
     */
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            //如果不等待
            if (mills <= 0) {
                //判断连接池中是否有空闲连接
                while (pool.isEmpty()) {
                    //线程进行等待
                    pool.wait();
                }
                //如果有空闲链接，直接返回第一个
                return pool.removeFirst();
            } else {
                //目标休眠时间，毫秒书
                long future = System.currentTimeMillis() + mills;
                //期望休眠毫秒数
                long remaining = mills;
                //如果可用连接为0 且 休眠时间 大于 0
                while (pool.isEmpty() && remaining > 0) {
                    //线程进行定时 等待
                    pool.wait(remaining);
                    //目标时间 - 当前时间
                    remaining = future - System.currentTimeMillis();
                }
                //返回的连接
                Connection result = null;
                //如果以上休眠动作都完成，依然没有可用线程，返回空 null
                if (!pool.isEmpty()) {
                    //有可用线程，返回连标第一个
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }
}
