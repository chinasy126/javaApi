package com.example.java.mapper;
import java.util.Date;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java.entity.Product;
import com.example.java.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    IPage<User> selectListByPage(@Param("page") Page<User> page, @Param("user") User user);

  //  IPage<User> selectListByUsername(@Param("page") Page<User> page, @Param("user") User user);
//    @Select("select * from user")
//     List<User> find();
//
//
//    @Insert("insert into user values (#{id},#{username} ,#{password},#{birthday})")
//     int insert(User user);
//
//    @Delete("delete from user where id= #{id}")
//    int delete(int id);


    @Select("select * from t_user")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "birthday", property = "birthday"),
            @Result(column = "id", property = "orders", javaType = List.class,
                    many = @Many(select = "com.example.java.mapper.OrderMapper.selectByUid")
            )
    })
    List<User> selectAllUserAndOrders();

    @Select("select * from t_user where id = #{id}")
    User selectById(int id);

}
