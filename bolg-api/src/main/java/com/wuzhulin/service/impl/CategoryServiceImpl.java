package com.wuzhulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wuzhulin.dao.CategoryMapper;
import com.wuzhulin.entity.Category;
import com.wuzhulin.service.CategoryService;
import com.wuzhulin.vo.CategoryVo;
import com.wuzhulin.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public Result getAllCategory() {
        List<Category> categoryList = categoryMapper.selectList(null);
        List<CategoryVo> categoryVoList = copyList(categoryList);
        return Result.success(categoryVoList);
    }

    private List<CategoryVo> copyList(List<Category> categoryList) {
        return categoryList
                .stream()
                .map(this::copy)
                .collect(Collectors.toList());
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
