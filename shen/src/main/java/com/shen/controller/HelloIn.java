package com.shen.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shen.service.SimpleService;
import com.shen.utils.AuthChecker;
import com.shen.utils.Log;
import com.shen.utils.RedisCacheUtil;

@RestController
public class HelloIn {
	
	@Autowired  
    private SimpleService simpleService;  
	
	@Autowired  
	private RedisCacheUtil redisCache;
      
    @RequestMapping("/syx")
//    @Log(operationType="add操作:",operationName="添加用户") 
    public String syx(HttpServletRequest request){   
        String wwww = simpleService.sayHello2User("1");
        System.out.println(wwww);
        redisCache.setCacheValue("key","value");
        request.setAttribute("newUserName", "1111111");
        return "helloworld";  
    }  
	
	@RequestMapping("helloworld")
    public String getNewName(HttpServletRequest request){

           request.setAttribute("newUserName", "1111111");

           return "helloworld";

    }
	
	 public static void main(String[] args) {
	        String name = "hzh";
	        Logger logger = LoggerFactory.getLogger(HelloIn.class);
	        logger.debug("Hello World");
	        logger.info("hello {}", name);
	    }

}
