package com.shen.jmm;

import java.util.concurrent.TimeUnit;

public class Join {
    public static void main(String[] args) throws Exception{
        //默认去main线程
        Thread previous = Thread.currentThread();
        for(int i =0; i<10; i++){
            //每个线程拥有前一个线程的饮用，需要等待前一个线程的终止，才能从等待中返回
            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            thread.start();
            //传给下个线程，作为join线程的变量
            previous = thread;
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName() + "terminate.");

    }
    static class Domino implements Runnable {
        private Thread thread;
        public Domino(Thread thread){
            this.thread = thread;
        }

        public void run(){
            try {
                thread.join();
            }catch (InterruptedException e){

            }
            System.out.println(Thread.currentThread().getName() + "terminate.");
        }
    }
}
