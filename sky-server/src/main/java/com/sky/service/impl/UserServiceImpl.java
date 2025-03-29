package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @description: 用户相关接口服务层的实现类
 * @author: Excell
 * @data 2025年03月28日 10:17
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    private static final String WEIXIN_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * @description: 用户登录
     * @title: login
     * @param: [userLoginDTO]
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        // 1.请求微信服务器获取openid
        String openid = getOpenid(userLoginDTO.getCode());
        // 1.1 openid为空，抛出异常
        if (openid == null || openid.isEmpty()) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 1.2 openid不为空
        // 2.根据openid查询数据库并返回user对象
        User user = userMapper.getUser(openid);
        // 3.user对象是否为空
        // 3.1为空，插入数据库，并返回user对象
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
            return user;
        }
        // 3.2不为空，返回user对象
        return user;
    }

    private String getOpenid(String code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WEIXIN_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
