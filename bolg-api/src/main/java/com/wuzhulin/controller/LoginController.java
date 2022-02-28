package com.wuzhulin.controller;

import com.wuzhulin.service.LoginService;
import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 登录验证
     */
    @PostMapping("login")
    public Result login(@RequestBody LoginParam loginParam) {
        return loginService.login(loginParam);
    }

    /**
     * 退出登录
     */
    @GetMapping("logout")
    public Result logout(@RequestHeader("Authorization") String token) {
        return loginService.logout(token);
    }

    /**
     * 注册
     */
    @PostMapping("register")
    public Result register(@RequestBody LoginParam loginParam) {
        return loginService.register(loginParam);
    }
}
