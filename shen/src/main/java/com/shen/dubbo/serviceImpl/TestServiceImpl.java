package com.shen.dubbo.serviceImpl;

import org.springframework.stereotype.Service;

import com.shen.dubbo.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    public String getName() {
        return "shenyang";
    }

}
