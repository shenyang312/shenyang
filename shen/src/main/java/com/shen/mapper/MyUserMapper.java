package com.shen.mapper;

import com.shen.model.MyUser;

public interface MyUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(MyUser record);

    int insertSelective(MyUser record);

    MyUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MyUser record);

    int updateByPrimaryKey(MyUser record);
}