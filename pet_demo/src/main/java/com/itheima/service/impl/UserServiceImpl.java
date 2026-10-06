package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.UserMapper;
import com.itheima.common.BusinessException;
import com.itheima.dto.LoginDTO;
import com.itheima.dto.RegisterDTO;
import com.itheima.dto.UserUpdateDTO;
import com.itheima.entity.User;
import com.itheima.service.UserService;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void register(RegisterDTO registerDTO) {
        //校验用户名是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        if(this.count(queryWrapper) > 0){
            throw new BusinessException("用户名已存在");
        }
        //注册用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordUtil.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setStatus(1);
        user.setRole("user");
        this.save(user);
    }

    @Override
    public String Login(LoginDTO loginDTO) {
        //查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户名不存在");
        }
        //校验密码
        if (!passwordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        //登录成功
        if (user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        stringRedisTemplate.opsForValue().set("token:"+user.getId(), token,2, TimeUnit.HOURS);
        return token;
    }

    @Override
    public String adminLogin(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (!passwordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        if (!"admin".equals(user.getRole())) {
            throw new BusinessException("当前账号无权登录");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        stringRedisTemplate.opsForValue().set("token:" + user.getId(), token, 2, TimeUnit.HOURS);
        return token;
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public void updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = this.getById(userId);
        if(user == null){
            throw new BusinessException("用户不存在");
        }
        if(StringUtils.hasText(userUpdateDTO.getNickname())){
            user.setNickname(userUpdateDTO.getNickname());

        }
        if(StringUtils.hasText(userUpdateDTO.getEmail())){
            user.setEmail(userUpdateDTO.getEmail());
        }
        if(StringUtils.hasText(userUpdateDTO.getPhone())){
            user.setPhone(userUpdateDTO.getPhone());

        }

        this.updateById(user);
    }

    @Override
    public void updateAvatar(Long userId, String avatar) {

        User user = new User();
        user.setId(userId);
        user.setAvatar(avatar);
        this.updateById(user);
    }
    @Override
    public void logout(Long userId) {
        stringRedisTemplate.delete("token:"+userId);
    }
}