package com.wuzhulin.controller;

import com.wuzhulin.service.ArticleService;
import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.param.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页  文章列表
     * @param pageVo
     * @return
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParam pageVo) {
        return articleService.listArticle(pageVo);
    }

    /**
     * 首页 最热文章
     * @return
     */
    @PostMapping("hot")
    public Result hotArticles() {
        int limit = 5;
        return articleService.findhot(limit);
    }

    /**
     * 首页 最新文章
     */
    @PostMapping("new")
    public Result newArticles() {
        int limit = 5;
        return articleService.findnew(limit);
    }

    /**
     * 首页 文章归档
     */
    @PostMapping("listArchives")
    public Result ListArticles() {
        return articleService.findArticles();
    }

    /**
     * 文章详情
     */
    @PostMapping("view/{id}")
    public Result view(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }
}
