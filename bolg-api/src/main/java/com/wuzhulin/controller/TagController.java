package com.wuzhulin.controller;

import com.wuzhulin.service.TagService;
import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("hot")
    public Result hot() {
        int limit = 6;
        List<TagVo> tagVoList = tagService.findHot(limit);
        return Result.success(tagVoList);
    }

    @GetMapping
    public Result getAll() {
        return tagService.getAll();
    }

    @GetMapping("detail")
    public Result getTagAll() {
        return tagService.getTagAll();
    }

    @GetMapping("detail/{id}")
    public Result getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }
}
