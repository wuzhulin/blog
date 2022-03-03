package com.wuzhulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuzhulin.dao.ArticleMapper;
import com.wuzhulin.dao.ArticleTagMapper;
import com.wuzhulin.dao.SysUserMapper;
import com.wuzhulin.entity.Article;
import com.wuzhulin.entity.ArticleBody;
import com.wuzhulin.entity.ArticleTag;
import com.wuzhulin.entity.SysUser;
import com.wuzhulin.service.*;
import com.wuzhulin.util.UserThreadLocal;
import com.wuzhulin.vo.*;
import com.wuzhulin.vo.dos.Articles;
import com.wuzhulin.vo.param.ArticleBodyParam;
import com.wuzhulin.vo.param.ArticleParam;
import com.wuzhulin.vo.param.PageParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
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
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    /*@Override
    public Result listArticle(PageParam pageVo) {
        //分页查询文章
        Page<Article> page = new Page<>(pageVo.getPage(), pageVo.getPageSize());
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper();
        if (pageVo.getCategoryId() != null) {
            qw.eq(Article::getCategoryId, pageVo.getCategoryId());
        }
        List<Long> articleIdList = new ArrayList<>();
        if (pageVo.getTagId() != null) {
            LambdaQueryWrapper<ArticleTag> tqw = new LambdaQueryWrapper<>();
            tqw.eq(ArticleTag::getTagId,pageVo.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(tqw);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList.size() > 0) {
                qw.in(Article::getId,articleIdList);
            }
        }

        //按照置顶和创建时间排序
        qw.orderByDesc(Article::getWeight,Article::getCreateDate);
        IPage<Article> articleIPage = articleMapper.selectPage(page,qw);
        List<Article> articleList = articleIPage.getRecords();

        //将article转成articleVo类型
        List<ArticleVo> articleVoList = copyList(articleList);
        return Result.success(articleVoList);
    }*/

    @Override
    public Result listArticle(PageParam pageVo) {
        Page<Article> page = new Page<>(pageVo.getPage(), pageVo.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page,pageVo.getCategoryId(),pageVo.getTagId(),pageVo.getYear(),pageVo.getMonth());
        return Result.success(copyList(articleIPage.getRecords()));
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
        threadService.updateViewCount(articleMapper,article);
        ArticleVo articleVo = copy(article);
        //获取文章的类别信息
        CategoryVo categoryVo = categoryService.findCategoryById(article.getCategoryId());
        articleVo.setCategory(categoryVo);
        //获取文章的内容
        ArticleBodyVo articleBodyVo = articleBodyService.findByArticleBody(article.getBodyId());
        articleVo.setBody(articleBodyVo);
        return Result.success(articleVo);
    }

    @Override
    public Result insertArticle(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        BeanUtils.copyProperties(articleParam,article);
        //设置文章创建时间
        article.setCreateDate(System.currentTimeMillis());
        //初始化阅读量和评论数为0
        article.setViewCounts(0);
        article.setCommentCounts(0);
        //设置作者id
        article.setAuthorId(sysUser.getId());
        articleMapper.insert(article);
        //设置文章是否置顶
        article.setWeight(Article.Article_Common);
        //设置文章类别
        article.setCategoryId(articleParam.getCategory().getId());
        //将文章内容插入body表单中
        ArticleBody body = new ArticleBody();
        body.setContent(articleParam.getBody().getContent());
        body.setContentHtml(articleParam.getBody().getContentHtml());
        body.setArticleId(article.getId());
        articleBodyService.insertBody(body);
        article.setBodyId(body.getId());
        articleMapper.updateById(article);

        //更新文章绑定标签表
        List<TagVo> tags = articleParam.getTags();
        if (tags != null && !tags.isEmpty()) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(article.getId());
                articleTagService.insert(articleTag);
            }
        }
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }


    private List<ArticleVo> copyList(List<Article> articleList) {
        return articleList.stream()
                .map(this::copy)
                .collect(Collectors.toList());
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
