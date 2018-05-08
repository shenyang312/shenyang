package com.shen.aop.aspectj;

public class CustomerManagerImpl implements CustomerManager {

	public void addCustomer(String name, String password) {
		System.out.print("加入了客户： " + name + "密码是： " + password);

	}

	public void deleteCustomer(String name) {

		System.out.println("删除了客户： " + name);
	}

	public String getCustomerById(int id) {
		System.out.println("找到了用户");
		return "shen";
	}

	public void updateCustomer(int id, String name, String password) {

		System.out.println("更改了用户基本信息");
	}

}