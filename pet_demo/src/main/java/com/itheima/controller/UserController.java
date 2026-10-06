package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.dto.LoginDTO;
import com.itheima.dto.RegisterDTO;
import com.itheima.dto.UserUpdateDTO;
import com.itheima.entity.User;
import com.itheima.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody RegisterDTO registerDTO){
        userService.register(registerDTO);
        return Result.success("注册成功");
    }
    @PostMapping("/login")
    public Result<String> Login(@Validated @RequestBody LoginDTO loginDTO){
        String token = userService.Login(loginDTO);
        return Result.success(token);
    }
    @PostMapping("/admin/login")
    public Result<String> adminLogin(@Validated @RequestBody LoginDTO loginDTO){
        String token = userService.adminLogin(loginDTO);
        return Result.success(token);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request){
        Long userId = Long.parseLong(request.getAttribute("userId").toString());
        return Result.success(userService.getUserInfo(userId));
    }

    @PutMapping("/update")
    public Result<?> updateUserInfo(HttpServletRequest request, @Validated @RequestBody UserUpdateDTO userUpdateDTO){
        Long userId = Long.parseLong(request.getAttribute("userId").toString());
        userService.updateUser(userId, userUpdateDTO);
        return Result.success("更新成功");
    }

    @PostMapping("/uploadAvatar")
    public Result<String> updateUserAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        Long userId = Long.parseLong(request.getAttribute("userId").toString());
        String avatarUrl ="http://your-domain.com/avatar"+userId+".jpg";
        userService.updateAvatar(userId, avatarUrl);
        return Result.success(avatarUrl);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        Long userId = Long.parseLong(request.getAttribute("userId").toString());
        userService.logout(userId);
        return Result.success("退出成功");
    }
}