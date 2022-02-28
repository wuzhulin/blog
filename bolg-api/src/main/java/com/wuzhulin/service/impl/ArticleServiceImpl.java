package com.wuzhulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuzhulin.dao.ArticleMapper;
import com.wuzhulin.dao.SysUserMapper;
import com.wuzhulin.entity.Article;
import com.wuzhulin.entity.SysUser;
import com.wuzhulin.service.*;
import com.wuzhulin.vo.*;
import com.wuzhulin.vo.dos.Articles;
import com.wuzhulin.vo.param.PageParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleBodyService articleBodyService;

    @Override
    public Result listArticle(PageParam pageVo) {
        //分页查询文章
        Page<Article> page = new Page<>(pageVo.getPage(), pageVo.getPageSize());
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper();
        //按照置顶和创建时间排序
        qw.orderByDesc(Article::getWeight,Article::getCreateDate);
        IPage<Article> articleIPage = articleMapper.selectPage(page,qw);
        List<Article> articleList = articleIPage.getRecords();

        //将article转成articleVo类型
        List<ArticleVo> articleVoList = copyList(articleList);
        return Result.success(articleVoList);
    }

    @Override
    public Result findhot(int limit) {
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(Article::getViewCounts);
        qw.select(Article::getId,Article::getTitle);
        qw.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(qw);
        List<ArticleVo> articleVoList = copyList(articles);
        return Result.success(articleVoList);
    }

    @Override
    public Result findnew(int limit) {
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(Article::getCreateDate);
        qw.select(Article::getId,Article::getTitle);
        qw.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(qw);
        List<ArticleVo> articleVoList = copyList(articles);
        return Result.success(articleVoList);
    }

    @Override
    public Result findArticles() {
        List<Articles> articlesList = articleMapper.findArticles();
        return Result.success(articlesList);
    }

    @Override
    public Result findArticleById(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article);
        //获取文章的类别信息
        CategoryVo categoryVo = categoryService.findCategoryById(article.getCategoryId());
        articleVo.setCategory(categoryVo);
        //获取文章的内容
        ArticleBodyVo articleBodyVo = articleBodyService.findByArticleBody(article.getBodyId());
        articleVo.setBody(articleBodyVo);
        return Result.success(articleVo);
    }

    private List<ArticleVo> copyList(List<Article> articleList) {
        List<ArticleVo> collect = articleList.stream()
                .map(this::copy)
                .collect(Collectors.toList());
        return collect;
    }

    private ArticleVo copy(Article article) {
        //在这可以更改是否开启作者和标签
        boolean isTag = true , isAuthor = true;

        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        //设置时间
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH-mm"));
        //判断是否有标签属性
        if(isTag) {
            Long articleId = article.getId();
            List<TagVo> tagByArticleId = tagService.findTagByArticleId(articleId);
            articleVo.setTags(tagByArticleId);
        }
        //判断是否有作者信息
        if(isAuthor) {
            Long authorId = article.getAuthorId();
            SysUser userById = sysUserService.findUserById(authorId);
            articleVo.setAuthor(userById.getNickname());
        }
        return articleVo;
    }
}
