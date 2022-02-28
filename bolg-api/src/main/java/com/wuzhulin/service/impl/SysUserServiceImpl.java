package com.wuzhulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wuzhulin.dao.SysUserMapper;
import com.wuzhulin.entity.SysUser;
import com.wuzhulin.service.LoginService;
import com.wuzhulin.service.SysUserService;
import com.wuzhulin.util.ErrorCode;
import com.wuzhulin.vo.LoginUserVo;
import com.wuzhulin.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if(sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("wuzhulin");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        qw.select(SysUser::getNickname,SysUser::getId,SysUser::getAccount,SysUser::getAvatar)
                .eq(SysUser::getAccount,account)
                .eq(SysUser::getPassword,password);
        return sysUserMapper.selectOne(qw);
    }

    @Override
    public Result findUserByToken(String token) {
        //根据token去Redis查找用户信息
        SysUser sysUser = loginService.checkToken(token);
        if(sysUser == null) {
            return Result.fail(ErrorCode.TOKEN_EROOR.getCode(), ErrorCode.TOKEN_EROOR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(sysUser, loginUserVo);
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        qw.eq(SysUser::getAccount,account);
        SysUser sysUser = sysUserMapper.selectOne(qw);
        return sysUser;
    }

    @Override
    public void save(SysUser sysUser) {
        sysUserMapper.insert(sysUser);
    }
}
