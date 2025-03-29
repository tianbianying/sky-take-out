package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @description: 用户相关接口服务层的接口
 * @author: Excell
 * @date: 2025年03月28日 10:17
 */
public interface UserService {
    /**
     * @description: 用户登录
     * @title: login
     * @param: [userLoginDTO]
     */
    User login(UserLoginDTO userLoginDTO);
}
