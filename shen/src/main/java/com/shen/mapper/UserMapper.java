package com.shen.mapper;

import org.springframework.stereotype.Repository;


import com.shen.model.UserModel;

@Repository
public interface UserMapper {
	/**
	 * 根据id查找用户信息
	 * 
	 * @author sunyx
	 * @param id
	 * @return
	 * @since JDK 1.8
	 */
	UserModel findUserById(String id);

}
