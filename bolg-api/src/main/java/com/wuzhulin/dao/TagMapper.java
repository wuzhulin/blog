package com.wuzhulin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuzhulin.entity.Tag;
import com.wuzhulin.vo.TagVo;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> findTagByArticleId(Long articleId);

    List<Long> findHotId(int limit);
}
