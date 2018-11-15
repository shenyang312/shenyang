package com.shen.jmm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 */
public class Cache {
    static Map<String,Object> map = new HashMap<>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static Lock r = rwl.readLock();
    static Lock w = rwl.writeLock();
    //获取一个key对应的value
    public static final Object get(String key){
        r.lock();
        try {
            return map.get(key);
        }finally {
            r.unlock();
        }
    }
    //设置key对应的value，并返回旧的value
    public static final Object put(String key,Object value){
        w.lock();
        try {
            return map.put(key,value);
        }finally {
            w.unlock();
        }
    }

    //清空所有的内容
    public static final void clear(){
        w.lock();
        try {
            map.clear();
        }finally {
            w.unlock();
        }
    }

        public static void main(String[] args) throws InterruptedException {
            RunThread thread = new RunThread();

            thread.start();
            Thread.sleep(1000);
            thread.setRunning(false);

            System.out.println("已经赋值为false");
        }

    static class RunThread extends Thread {
        private boolean isRunning = true;
        int m;
        public boolean isRunning() {
            return isRunning;
        }
        public void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }
        @Override
        public void run() {
            System.out.println("进入run了");
            while (isRunning == true) {
                int a=2;
                int b=3;
                int c=a+b;
                m=c;
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                System.out.println(m);
            }
            System.out.println(m);
            System.out.println("线程被停止了！");
        }
    }
}
