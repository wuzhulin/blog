package com.wuzhulin.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.datatype.jsr310.DecimalUtils;
import com.wuzhulin.entity.SysUser;
import com.wuzhulin.service.LoginService;
import com.wuzhulin.service.SysUserService;
import com.wuzhulin.util.ErrorCode;
import com.wuzhulin.util.JWTUtils;
import com.wuzhulin.vo.Result;
import com.wuzhulin.vo.param.LoginParam;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    private static final String salt = "mszlu!@#";

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Result login(LoginParam loginParam) {
        //拿到账号和密码
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        //对账号和密码做非空判断
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //对密码进行加密处理
        password = DigestUtils.md5Hex(password+salt);
        //根据账号和密码查询对应的用户信息
        SysUser sysUser =sysUserService.findUser(account,password);
        //当sysUser为空时，说明数据库中没有该用户
        if(sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //根据用户信息生成对应的Token并返回前端和
        String token = JWTUtils.createToken(sysUser.getId());
        //保存到redis
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        //将token返回给前端,之后需要登录的请求都需要携带这个token 进行验证
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        if(map == null) return null;
        String s = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(s == null) return null;
        SysUser sysUser = JSON.parseObject(s,SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        //拿到所有数据并校验
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickName = loginParam.getNickname();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickName))
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        //判断该账户是否存在
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if(sysUser != null)
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        //将用户信息保存到数据库
        sysUser = new SysUser();
        sysUser.setNickname(nickName);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);

        //生成token并保持的Redis中
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token,JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);
    }
}
