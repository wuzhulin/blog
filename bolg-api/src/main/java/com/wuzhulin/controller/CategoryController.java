package com.wuzhulin.controller;

import com.wuzhulin.service.CategoryService;
import com.wuzhulin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    private Result getCategoryList() {
        return categoryService.getAllCategory();
    }
}
