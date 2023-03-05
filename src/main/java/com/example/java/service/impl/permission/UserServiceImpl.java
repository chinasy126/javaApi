package com.example.java.service.impl.permission;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.User;
import com.example.java.mapper.UserMapper;
import com.example.java.service.permission.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}
