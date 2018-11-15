package com.shen.jmm;

/**
 * 双重检查锁   懒加载
 */
public class LazyLoadDemo {
    private volatile static Instance instance;//实例

    public static Instance getInstance(){
        if(instance == null){//不加volatile 。 会被JIT重排序，导致B线程访问不安全
            synchronized (LazyLoadDemo.class){
                if(instance == null){
                    instance = new Instance();
                }
            }
        }
        return instance;
    }

    static class Instance{

    }
}
