package com.itliang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itliang.reggie.dto.DishDto;
import com.itliang.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品，同时像dishflavor里插入数据，需要操作两张表
    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    DishDto getByIdWithFlavor(Long id);

    //更新菜品信息和口味信息
    void updateWithFlavor(DishDto dishDto);

}
