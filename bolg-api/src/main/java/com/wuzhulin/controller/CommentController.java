package com.wuzhulin.controller;

import com.wuzhulin.service.CommentService;
import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.param.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable Long id) {
        return commentService.getByArticleId(id);
    }

    @PostMapping("create/change")
    public Result CreateComment(@RequestBody CommentParam commentParam) {
        return commentService.insertComment(commentParam);
    }
}
