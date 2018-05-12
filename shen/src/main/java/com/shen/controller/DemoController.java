package com.shen.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shen.utils.AuthChecker;

@RestController
public class DemoController {
	@RequestMapping("/aop/http/alive")
	public String alive() {
		return "ok";
	}

	@AuthChecker
	@RequestMapping(value = "/aop/http/user_info")
	public String callSomeInterface() {
		return "ok in user_info.";
	}

}
