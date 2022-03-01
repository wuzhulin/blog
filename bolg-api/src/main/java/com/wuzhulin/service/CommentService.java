package com.wuzhulin.service;

import com.wuzhulin.vo.Result;

public interface CommentService {
    Result getByArticleId(Long id);
}
