package com.wuzhulin.service;

import com.wuzhulin.vo.ArticleBodyVo;
import com.wuzhulin.vo.param.ArticleBodyParam;

public interface ArticleBodyService {
    ArticleBodyVo findByArticleBody(Long id);

    void insertBody(ArticleBodyParam body,Long id);

    Long findByArticleBodyId(Long id);
}
