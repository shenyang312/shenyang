package com.shen.jmm;

public class LazyLoadDemoTwo {
    private static class InstanceHolder{
        public static LazyLoadDemo.Instance instance = new LazyLoadDemo.Instance();
    }

    public static LazyLoadDemo.Instance getInstance(){
        return InstanceHolder.instance;
    }
}
