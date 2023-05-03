package com.itliang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itliang.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
