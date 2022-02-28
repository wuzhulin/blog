package com.wuzhulin.controller;

import com.wuzhulin.service.SysUserService;
import com.wuzhulin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @GetMapping("currentUser")
    public Result UserInfo(@RequestHeader("Authorization") String token) {
        return sysUserService.findUserByToken(token);
    }

}
