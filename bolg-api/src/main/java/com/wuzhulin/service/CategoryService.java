package com.wuzhulin.service;

import com.wuzhulin.vo.CategoryVo;
import com.wuzhulin.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long Id);

    Result getAllCategoryVO();

    Result getAllCategory();

    Result getCategoryById(Long id);
}
