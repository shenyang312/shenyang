package com.shen.jmm;

public class VolatileTest {
    public static volatile int race = 0;
    public static void increase(){
        race++;
    }
    private static final int THREADS_COUNT = 2;
    public static void main(String[] str){
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++){
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i<10; i++){
                        increase();
                    }
                }
            });
            threads[i].start();
        }
        //等待所有累加线程都结束
        while (Thread.activeCount() > 1)
            //线程让出
            Thread.yield();
        System.out.println(race);
    }
}
