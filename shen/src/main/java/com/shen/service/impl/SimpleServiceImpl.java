package com.shen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shen.mapper.UserMapper;
import com.shen.model.UserModel;
import com.shen.service.SimpleService;

/**  
 * 简单业务逻辑的实现类 
 * 
 * @author sunyx  
 * @since JDK 1.8  
 */  
@Service("simpleService")  
public class SimpleServiceImpl implements SimpleService {  
      
    @Autowired
    private UserMapper userMapper;  
  
    public String sayHello2User(String id) {  
    	UserModel user = userMapper.findUserById(id);
        System.out.println("hello:"+user.getName());  
        return "ok";  
    }  
  
}  
