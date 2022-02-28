package com.wuzhulin.service;

import com.wuzhulin.entity.SysUser;
import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.param.LoginParam;

public interface LoginService {

    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
