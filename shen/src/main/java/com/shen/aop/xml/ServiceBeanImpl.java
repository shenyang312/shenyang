package com.shen.aop.xml;

import java.util.HashMap;
import java.util.Map;

public class ServiceBeanImpl implements ServiceBean {

	private String dir;
	private Map<String, String> map = new HashMap<String, String>();

	public void addUser(String userName, String password) {
		if (!map.containsValue(userName))
			map.put(userName, password);
		else
			throw new RuntimeException("user has already exited!");
		
		System.out.println("新增用户：userName="+userName+"-----------password="+password);

	}

	public void deleteUser(String userName) {
		if (!map.containsKey(userName))
			throw new RuntimeException("user isn't exited");
		map.remove(userName);
		System.out.println("删除用户：userName="+userName);
	}

	public boolean findUser(String userName) {
		System.out.println("查询当前用户是否存在：userName="+userName+"-------"+(map.containsKey(userName)?"存在":"不存在"));
		return map.containsKey(userName);
	}

	public String getPassword(String userName) {
		System.out.println("去除用户密码：userName="+userName);
		return (String) map.get(userName);
	}

	public void setDir(String dir) {

		this.dir = dir;
		System.out.println("set user to:" + dir);
	}
}