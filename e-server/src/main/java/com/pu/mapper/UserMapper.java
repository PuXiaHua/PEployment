package com.pu.mapper;

import com.pu.epojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from sys_user where email=#{username} or phone=#{username}")
    User getUser(String username);

    @Insert("insert into sys_user(role, phone, email, password_hash,status ,create_time, update_time)values (#{role},#{phone},#{email},#{password},#{status},#{createTime},#{updateTime})")
    void registy(User user);

    @Select("select * from sys_user where id=#{id}")
    User getUserById(Long id);
}
