package com.shen.jmm.threadPool;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 执行流程
 * 创建 N个工作线程—>执行然后让它等待任务->这时候等待的工作是空->当有一个任务进来的时候->插入等待的工作列表里面 -> 去唤醒 N个工作线程随机一个 —> 去执行工作->执行完毕继续进入等待状态。
 * @param <Job>
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
    //线程池最大限制数
    private static final int MAX_WORKER_NUMBERS = 10;
    //线程池默认的数量
    private static final int DEFAULT_WORKER_NUMBERS = 5;
    //线程池最小的数量
    private static final int MIN__WORKER_NUMBERS = 1;
    //这是一个工作列表，将会向里面插入工作
    private final LinkedList<Job> jobs = new LinkedList<>();
    //工作者列表
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());
    //工作者线程的数量
    private int workerNum = DEFAULT_WORKER_NUMBERS;
    //线程编号生成
    private AtomicLong threadNum = new AtomicLong();

    public DefaultThreadPool(){
        initializeWokers(DEFAULT_WORKER_NUMBERS);
    }

    public DefaultThreadPool(int num){
        workerNum = num > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS : num < MIN__WORKER_NUMBERS ? MIN__WORKER_NUMBERS : num;
        initializeWokers(workerNum);
    }

    /**
     * 执行一个Job，这个Job需要实现Runnbale
     * @param job
     */
    @Override
    public void execute(Job job) {
        if(job != null){
            //添加一个工作，然后进行通知
            synchronized (jobs){
                System.out.println("就在这个时候进来一个工作");
                jobs.addLast(job);
                System.out.println("赶紧去workers，工棚 一共有5个线程 ，就代表五个工人，去喊一个 worker工人起来干活");
                jobs.notify();
            }
        }
    }

    /**
     * 关闭线程池
     */
    @Override
    public void shutdown() {
        for (Worker worker : workers){
            worker.shutdown();
        }

    }

    /**
     * 增加工作者线程
     * @param num
     */
    @Override
    public void addWorkers(int num) {
        synchronized (jobs){
            //限制新增的Worker数量不能超过最大值
            if(num + this.workerNum > MAX_WORKER_NUMBERS){
                num = MAX_WORKER_NUMBERS - this.workerNum;
            }
            initializeWokers(num);
            this.workerNum += num;
        }

    }

    /**
     * 减少工作者线程
     * @param num
     */
    @Override
    public void removeWorker(int num) {
        synchronized (jobs){
            if(num >= this.workerNum){
                throw new IllegalArgumentException("beyond workNum");
            }
            //按照给定的数量停止Worker
            int count = 0;
            while(count < num){
                Worker worker = workers.get(count);
                if(workers.remove(worker)){
                    worker.shutdown();
                    count++;
                }
            }
            this.workerNum -= count;
        }

    }

    /**
     * 得到正在等待执行的任务数量
     * @return
     */
    @Override
    public int getJobSize() {
        return jobs.size();
    }

    //初始化线程工作者
    private void initializeWokers(int num){
        System.out.println("一共招聘了"+num+"个工人-线程");
        for (int i = 0; i<num; i++){
            Worker worker = new Worker();
            //工作者列表
            workers.add(worker);
            Thread thread = new Thread(worker,"ThreadPool-Worker-"+ threadNum.incrementAndGet());
            thread.start();
        }
    }

    //工作者，负责消费任务
    class Worker implements Runnable{
        //是否工作
        private volatile boolean running = true;
        public void run(){
            while (running){
                Job job = null;
                //jobs工作列表
                synchronized (jobs){
                    //如果工作者列表是空的，那么就wait
                    while (jobs.isEmpty()){
                        try{
//                            System.out.println("任务队列中为空，等待来任务......");
                            System.out.println("workers里面等待的 worker干活的 闲完了！！");
                            jobs.wait();
                        }catch (InterruptedException ex){
                            System.out.println("线程中断");
                            //感知到外部对WorkerThread的中断操作，返回
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    System.out.println("就在这个时候 工棚里面的工人被喊醒了，jobs任务不为空了，不会再让jobs工作中断，准备开始干活");
                    //取出一个job（这里是在线程的链表里拿出一个）
                    System.out.println("先把jobs里面第一个任务拿出来");
                    job = jobs.removeFirst();
                }
                if(job != null){
                    try {
                        System.out.println("开始干 拿出来的任务");
                        job.run();
                    }catch (Exception ex){

                    }
                }
            }
        }
        public void shutdown(){
            running = false;
        }
    }
}
