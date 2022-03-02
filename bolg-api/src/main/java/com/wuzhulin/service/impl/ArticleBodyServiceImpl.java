package com.wuzhulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wuzhulin.dao.ArticleBodyMapper;
import com.wuzhulin.entity.ArticleBody;
import com.wuzhulin.service.ArticleBodyService;
import com.wuzhulin.vo.ArticleBodyVo;
import com.wuzhulin.vo.param.ArticleBodyParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyServiceImpl implements ArticleBodyService {
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Override
    public ArticleBodyVo findByArticleBody(Long id) {
        ArticleBody articleBody = articleBodyMapper.selectById(id);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        BeanUtils.copyProperties(articleBody,articleBodyVo);
        return articleBodyVo;
    }

    @Override
    public void insertBody(ArticleBody articleBody) {
        articleBodyMapper.insert(articleBody);
    }

    @Override
    public Long findByArticleBodyId(Long id) {
        ArticleBody articleBody = articleBodyMapper.selectById(id);
        return articleBody.getId();
    }
}
