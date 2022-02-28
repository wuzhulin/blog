package com.wuzhulin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuzhulin.entity.Article;
import com.wuzhulin.vo.dos.Articles;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    @Select("select year(from_unixtime(create_date/1000)) as year,month(from_unixtime(create_date/1000)) as month,count(*) as count from ms_article group by year,month order by year,month desc")
    List<Articles> findArticles();
}
