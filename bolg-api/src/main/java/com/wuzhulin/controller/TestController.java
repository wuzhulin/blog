package com.wuzhulin.controller;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.spi.db.BindingInfo;
import com.wuzhulin.entity.SysUser;
import com.wuzhulin.util.UserThreadLocal;
import com.wuzhulin.vo.param.LoginParam;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("test")
public class TestController {

    @PostMapping
    private String test() {
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return "成功访问!";
    }

    @GetMapping("test1")
    private String getName(@Validated(LoginParam.test1.class) @RequestBody LoginParam loginParam , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String,String> map = new HashMap<>();
            bindingResult.getFieldErrors().forEach((item)->{
                String message = item.getDefaultMessage();
                String field = item.getField();
                map.put(field,message);
            });
            return JSON.toJSONString(map);
        }
        return JSON.toJSONString(loginParam);
    }

    @GetMapping("test2")
    private String get(@Validated(LoginParam.test2.class) @RequestBody LoginParam loginParam , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String,String> map = new HashMap<>();
            bindingResult.getFieldErrors().forEach((item)->{
                String message = item.getDefaultMessage();
                String field = item.getField();
                map.put(field,message);
            });
            return JSON.toJSONString(map);
        }
        return JSON.toJSONString(loginParam);
    }
}
