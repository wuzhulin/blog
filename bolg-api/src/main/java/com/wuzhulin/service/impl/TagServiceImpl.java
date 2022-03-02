package com.wuzhulin.service.impl;

import com.wuzhulin.dao.TagMapper;
import com.wuzhulin.entity.Tag;
import com.wuzhulin.service.TagService;
import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.TagVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagByArticleId(Long articleId) {
        //mybatis不能进行多表查询，需要自己手写xml
        List<Tag> tagList = tagMapper.findTagByArticleId(articleId);
        List<TagVo> tagVoList = copyList(tagList);
        return tagVoList;
    }

    @Override
    public List<TagVo> findHot(int limit) {
        log.info("进入");
        List<Long> TagVoId= tagMapper.findHotId(limit);
        //根据标签Id查找标签的信息
        log.info(TagVoId.toString());
        List<Tag> tags = tagMapper.selectBatchIds(TagVoId);
        return copyList(tags);
    }

    @Override
    public Result getAll() {
        List<Tag> tagList = tagMapper.selectList(null);
        List<TagVo> tagVoList = copyList(tagList);
        return Result.success(tagVoList);
    }

    private List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> collect = tagList.stream()
                .map(this::copy)
                .collect(Collectors.toList());
        return collect;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
}
