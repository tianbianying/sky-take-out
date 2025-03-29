package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @description: User相关接口的持久层
 * @author: Excell
 * @date: 2025年03月28日 10:17
 */
@Mapper
public interface UserMapper {
    /**
     * @description: 根据openid查询用户
     * @title: getUser
     * @param: [openid]
     */
    @Select("select id, openid, name, phone, sex, id_number, avatar, create_time from user where openid = #{openid}")
    User getUser(String openid);

    @Insert("insert into user(openid, create_time) values  (#{openid}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);
}
