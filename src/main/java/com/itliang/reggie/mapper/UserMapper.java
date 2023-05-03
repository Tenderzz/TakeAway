package com.itliang.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itliang.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
