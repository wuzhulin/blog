package com.wuzhulin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wuzhulin.dao.ArticleMapper;
import com.wuzhulin.entity.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    @Async("taskExecutor")
    public void updateViewCount(ArticleMapper articleMapper , Article article)  {
        Article article1 = new Article();
        article1.setViewCounts(article.getViewCounts()+1);
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
        qw.eq(Article::getId,article.getId());
        qw.eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(article1,qw);
        try {
            // 睡眠5秒
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
