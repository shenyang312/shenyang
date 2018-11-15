package com.shen.jmm.connectionPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

public class ConnectionDriver {
    /**
     * jdk字节码代理
     */
    static class ConnectionHandler implements InvocationHandler {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //proxy：代理对象
            //method: 代理的方法对象
            //args: 方法调用时参数
            if (method.getName().equals("commit")) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            return null;
        }
    }

    //创建一个Connection 的代理，在commit时休眠100毫秒
    public static final Connection createConnection() {
        /**
         * @param   真实对象的类加载器
         * @param   真实对象实现的所有的接口,接口是特殊的类，使用Class[]装载多个接口
         * @param   接口，传递一个匿名内部类对象
         */
        return (Connection) Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(), new Class<?>[]{Connection.class}, new ConnectionHandler());
    }
}
