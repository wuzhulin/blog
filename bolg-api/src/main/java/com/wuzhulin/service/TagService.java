package com.wuzhulin.service;

import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagByArticleId(Long articleId);

    List<TagVo> findHot(int limit);

    Result getAll();

    void insertTags(List<TagVo> tags);
}
