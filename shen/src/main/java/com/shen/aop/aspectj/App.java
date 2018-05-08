package com.shen.aop.aspectj;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App   
{  
    public static void main( String[] args )  
    {  
        System.out.println( "Hello Spring AOP!" );  
        BeanFactory factory=new ClassPathXmlApplicationContext("spring-aop-test/spring-aop-aspectj.xml");  
        CustomerManager customerManager=(CustomerManager) factory.getBean("customerManager");  
        customerManager.getCustomerById(2015);  
          
          
          
          
    }  
}  
