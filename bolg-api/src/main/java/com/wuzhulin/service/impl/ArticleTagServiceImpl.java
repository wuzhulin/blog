package com.wuzhulin.service.impl;

import com.wuzhulin.dao.ArticleTagMapper;
import com.wuzhulin.entity.ArticleTag;
import com.wuzhulin.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl implements ArticleTagService {
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public void insert(ArticleTag articleTag) {
        articleTagMapper.insert(articleTag);
    }
}
