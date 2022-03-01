package com.wuzhulin.service;

import com.wuzhulin.entity.SysUser;
import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.UserVo;

import java.util.List;

public interface SysUserService {
    SysUser findUserById(Long authorId);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long authorId);
}
