package com.itliang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itliang.reggie.common.CustomException;
import com.itliang.reggie.entity.Category;
import com.itliang.reggie.entity.Dish;
import com.itliang.reggie.entity.Setmeal;
import com.itliang.reggie.mapper.CategoryMapper;
import com.itliang.reggie.service.CategoryService;
import com.itliang.reggie.service.DishService;
import com.itliang.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {

        //判断当前分类是否关联了菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count > 0){
            //说明已经关联了菜品
            throw new CustomException("当前分类项关联了菜品，不能删除");
        }

        //判断当前分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if(count1>0){
            //说明关联了套餐
            throw new CustomException("当前分类项关联了套餐，不能删除");

        }

        //正常删除
        super.removeById(id);
    }


}
