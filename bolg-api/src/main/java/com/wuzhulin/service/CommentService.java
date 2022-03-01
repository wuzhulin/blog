package com.wuzhulin.service;

import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.param.CommentParam;

public interface CommentService {
    Result getByArticleId(Long id);

    Result insertComment(CommentParam commentParam);
}
