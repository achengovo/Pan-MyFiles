package com.yunpan.achengovo.mapper;

import com.yunpan.achengovo.entity.AutoLogin;
import com.yunpan.achengovo.entity.User;
import com.yunpan.achengovo.entity.UserFile;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    //用户登录
    User login(User user);
    //用户注册
    Integer register(User user);
    //查询用户是否存在
    Integer hasUser(User user);
    //新建文件夹
    Integer newDir(String fileId, String fileLocation);
    //插入自动登录表
    Integer addAutoLogin(AutoLogin autoLogin);
    //查询是否能够自动登录
    AutoLogin isAutoLogin(AutoLogin autoLogin);
    //能够自动登录时通过id获取用户信息
    User loginById(User user);
    //修改头像
    Integer uploadAvatar(User user);
    //获取用户信息
    User getUserInfo(User user);
    //修改用户信息
    Integer changeUser(User user);
    //修改密码
    Integer changePass(User user);
    Integer changePassByPass(@Param("userId") Integer userId,@Param("userPass") String userPass,@Param("newPass") String newPass);
    //根据用户名获取用户信息
    User getUserByUserName(User user);
    //根据用户名修改密码
    Integer updatePassByUserName(User user);
}
