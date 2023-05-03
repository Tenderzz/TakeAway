package com.itliang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itliang.reggie.dto.SetmealDto;
import com.itliang.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐 同时保存菜品和套餐的关联关系
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐 同时删除套餐和菜品的关联数据
     */
    void removeWithDish(List<Long> ids);

    /**
     * 编辑套餐时的回显
     * @param id
     * @return
     */
    SetmealDto getByIdWithDish(Long id);

    /**
     * 更新套餐
     * @param setmealDto
     */
    void updateWithDish(SetmealDto setmealDto);
}
