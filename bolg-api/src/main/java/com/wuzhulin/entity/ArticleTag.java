package com.wuzhulin.entity;

import lombok.Data;

@Data
public class ArticleTag {
    private Long id;

    private Long articleId;

    private Long tagId;
}
