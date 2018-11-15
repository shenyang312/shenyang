package com.shen.jmm.threadPool;


/**
 * 工作类，用户在向线程池中发布任务时只需实现该接口即可，若不使用线程池
 * 则可直接使用该Job类开新线程
 * @author shuaicenglou
 *
 */
public interface Job extends Runnable {
}
