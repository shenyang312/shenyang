package com.shen.HttpUtiles;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpClientUtil {

    public static String doGet(String url) throws Exception {
        //声明
        ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
        //加入相关的https请求方式
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
        //发送请求即可
        org.apache.commons.httpclient.HttpClient httpclient = new org.apache.commons.httpclient.HttpClient();
        GetMethod httpget = new GetMethod(url);
        System.out.println("======url:" + url);
        try {
            httpclient.executeMethod(httpget);
            return httpget.getResponseBodyAsString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        } finally {
            httpget.releaseConnection();
        }
    }

    public static void main(String[] args) {
        IncrTest incrTest = new IncrTest();
        incrTest.concurrenceTest();
    }


    static class IncrTest {
        public void concurrenceTest() {
            /**
             * 模拟高并发情况代码
             */
            final AtomicInteger atomicInteger = new AtomicInteger(0);
            final CountDownLatch countDownLatch = new CountDownLatch(1000); // 相当于计数器，当所有都准备好了，再一起执行，模仿多并发，保证并发量
            final CountDownLatch countDownLatch2 = new CountDownLatch(1000); // 保证所有线程执行完了再打印atomicInteger的值
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            try {
                for (int i = 0; i < 1000; i++) {
                    System.out.println(i);
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                countDownLatch.await(); //一直阻塞当前线程，直到计时器的值为0,保证同时并发
                                System.out.println("进来的线程");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //每个线程增加1000次，每次加1
                            for (int j = 0; j < 1000; j++) {
                                atomicInteger.incrementAndGet();
                            }
                            countDownLatch2.countDown();
//                            try {
//                                URL url = new URL("https://chemlinked.com/node");
//                                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//                                System.out.println("发包成功！");
//                                SSLContext sc = SSLContext.getInstance("SSL");
//                                TrustManager[] tmArr = {new X509TrustManager() {
//                                    @Override
//                                    public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
//                                    }
//
//                                    @Override
//                                    public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
//                                    }
//
//                                    @Override
//                                    public X509Certificate[] getAcceptedIssuers() {
//                                        return null;
//                                    }
//                                }};
//                                sc.init(null, tmArr, new SecureRandom());
//                                conn.setSSLSocketFactory(sc.getSocketFactory());
//                                conn.connect();
//                                    BufferedInputStream bis = new BufferedInputStream(
//                                            conn.getInputStream());
//                                    byte[] bytes = new byte[1024];
//                                    int len = -1;
//                                    StringBuffer sb = new StringBuffer();
//
//                                    if (bis != null) {
//                                        if ((len = bis.read()) != -1) {
//                                            sb.append(new String(bytes, 0, len));
//                                            System.out.println("攻击成功！");
//                                            bis.close();
//                                        }
//                                    }
//                            } catch (MalformedURLException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            } catch (NoSuchAlgorithmException e) {
//                                e.printStackTrace();
//                            } catch (KeyManagementException e) {
//                                e.printStackTrace();
//                            }

                            String url = "http://chemlinked.com?referer=reg";

                            String body = null;
                            try {
                                body = HttpClientUtil.doGet(url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("交易响应结果：");
                            System.out.println(body);
                            System.out.println("-----------------------------------");
                        }
                    });
                    countDownLatch.countDown();
                }

                countDownLatch2.await();// 保证所有线程执行完
                System.out.println(atomicInteger);
                executorService.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
