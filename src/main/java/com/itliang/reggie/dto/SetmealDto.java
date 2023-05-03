package com.itliang.reggie.dto;

import com.itliang.reggie.entity.Setmeal;
import com.itliang.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
