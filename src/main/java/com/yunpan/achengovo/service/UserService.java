package com.yunpan.achengovo.service;

import com.yunpan.achengovo.entity.AutoLogin;
import com.yunpan.achengovo.entity.User;
import com.yunpan.achengovo.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    //用户登录
    public User login(User user){
        return userMapper.login(user);
    }
    //用户注册
    public Integer register(User user){
        return userMapper.register(user);
    }
    //查询是否存在用户
    public Integer hasUser(User user){
        return userMapper.hasUser(user);
    }
    //新建文件夹
    public Integer newDir(String fileId,String fileLocation){
        return userMapper.newDir(fileId,fileLocation);
    }
    //插入自动登录表
    public Integer addAutoLogin(AutoLogin autoLogin){
        return userMapper.addAutoLogin(autoLogin);
    }
    //查询是否能够自动登录
    public AutoLogin isAutoLogin(AutoLogin autoLogin){
        return userMapper.isAutoLogin(autoLogin);
    }
    //能够自动登录时通过id获取用户信息
    public User loginById(User user){
        return userMapper.loginById(user);
    }
    //修改头像
    public Integer uploadAvatar(User user){
        return userMapper.uploadAvatar(user);
    }
    //获取用户信息
    public User getUserInfo(User user){
        return userMapper.getUserInfo(user);
    }
    //修改用户信息
    public Integer changeUser(User user){
        return userMapper.changeUser(user);
    }
    //修改密码
    public Integer changePass(User user){
        return userMapper.changePass(user);
    }
    public Integer changePassByPass(Integer userId, String userPass, String newPass){
        return userMapper.changePassByPass(userId,userPass,newPass);
    }
    //根据用户名获取用户信息
    public User getUserByUserName(User user){
        return userMapper.getUserByUserName(user);
    }
    //根据用户名修改密码
    public Integer updatePassByUserName(User user){
        return userMapper.updatePassByUserName(user);
    }
}
