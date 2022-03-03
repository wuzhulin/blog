package com.wuzhulin.controller;

import com.wuzhulin.service.CategoryService;
import com.wuzhulin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result getCategoryList() {
        return categoryService.getAllCategoryVO();
    }

    @GetMapping("detail")
    public Result getDetail() {
        return categoryService.getAllCategory();
    }

    @GetMapping("detail/{id}")
    public Result getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
}
