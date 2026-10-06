package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.LoginDTO;
import com.itheima.dto.RegisterDTO;
import com.itheima.dto.UserUpdateDTO;
import com.itheima.entity.User;



public interface UserService extends IService<User> {

    // 注册
    void register(RegisterDTO registerDTO);
    //登录
    String Login(LoginDTO loginDTO);
    //管理员登录
    String adminLogin(LoginDTO loginDTO);

    //获取用户信息
    User getUserInfo(Long userId);

    //更新用户信息
    void updateUser(Long userId, UserUpdateDTO userUpdateDTO);

    //修改头像
    void updateAvatar(Long userId, String avatar);
    //退出登录
    void logout(Long userId);



}