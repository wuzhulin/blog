package com.wuzhulin.service;

import com.wuzhulin.entity.ArticleBody;
import com.wuzhulin.vo.ArticleBodyVo;
import com.wuzhulin.vo.param.ArticleBodyParam;

public interface ArticleBodyService {
    ArticleBodyVo findByArticleBody(Long id);

    void insertBody(ArticleBody articleBody);

    Long findByArticleBodyId(Long id);
}
