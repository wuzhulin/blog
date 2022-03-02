package com.wuzhulin.service;

import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.param.ArticleParam;
import com.wuzhulin.vo.param.PageParam;

public interface ArticleService {
    Result listArticle(PageParam pageVo);

    Result findhot(int limit);

    Result findnew(int limit);

    Result findArticles();

    Result findArticleById(Long articleId);

    Result insertArticle(ArticleParam articleParam);
}
