package com.itliang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itliang.reggie.dto.DishDto;
import com.itliang.reggie.entity.Dish;
import com.itliang.reggie.entity.DishFlavor;
import com.itliang.reggie.mapper.DishMapper;
import com.itliang.reggie.service.DishFlavorService;
import com.itliang.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImple extends ServiceImpl<DishMapper,Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息
        this.save(dishDto);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
           item.setDishId(dishDto.getId());
           return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到口味表
        dishFlavorService.saveBatch(flavors);

    }
    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     */
    public DishDto getByIdWithFlavor(Long id){
        //查询菜品基本信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询菜品口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 更新菜品信息和口味信息
     * @param dishDto
     * @return
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto){
        //更新dish表
        this.updateById(dishDto);

        //清理口味数据 重新添加
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
