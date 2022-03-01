package com.wuzhulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wuzhulin.dao.CommentMapper;
import com.wuzhulin.entity.Comment;
import com.wuzhulin.entity.SysUser;
import com.wuzhulin.service.ArticleService;
import com.wuzhulin.service.CommentService;
import com.wuzhulin.service.SysUserService;
import com.wuzhulin.util.UserThreadLocal;
import com.wuzhulin.vo.CommentVo;
import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.UserVo;
import com.wuzhulin.vo.param.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result getByArticleId(Long id) {
        LambdaQueryWrapper<Comment> qw = new LambdaQueryWrapper<>();
        qw.eq(Comment::getArticleId,id);
        qw.eq(Comment::getLevel,1);
        List<Comment> commentList = commentMapper.selectList(qw);
        List<CommentVo> commentVoList = copyList(commentList);
        return Result.success(commentVoList);
    }

    @Override
    public Result insertComment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentParam,comment);
        comment.setParentId(commentParam.getParent() == null ? 0: commentParam.getParent());
        comment.setToUid(commentParam.getToUserId() == null ? 0 : commentParam.getToUserId());
        //设置时间
        comment.setCreateDate(System.currentTimeMillis());
        //设置评论等级
        if(commentParam.getParent() == null || commentParam.getParent() == 0) {
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        //设置id
        comment.setAuthorId(sysUser.getId());
        commentMapper.insert(comment);
        return Result.success(null);
    }

    public List<CommentVo> copyList(List<Comment> commentList) {
        return commentList
                .stream()
                .map(this::copy)
                .collect(Collectors.toList());
    }

    public CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //格式话创建使时间
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm:ss"));
        //根据authorId查找作者信息
        UserVo author = sysUserService.findUserVoById(comment.getAuthorId());
        commentVo.setAuthor(author);
        //根据parentId查找子评论
        List<CommentVo> commentVoList = findCommentsByParentId(comment.getId());
        commentVo.setChildrens(commentVoList);
        //当level大于1时，还需要设置他的评论对象
        if(comment.getLevel() > 1) {
            UserVo userVoById = sysUserService.findUserVoById(comment.getToUid());
            commentVo.setToUser(userVoById);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> qw = new LambdaQueryWrapper<>();
        qw.eq(Comment::getParentId,id);
        qw.eq(Comment::getLevel,2);
        List<Comment> comments = commentMapper.selectList(qw);
        return copyList(comments);
    }

}
