package com.example.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.java.entity.Order;
import com.example.java.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper {

    @Select("select * from t_order where uid = #{uid}")
    List<Order> selectByUid(int uid);

    @Select("select * from t_order")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "order_time",property = "order_time"),
            @Result(column = "total",property = "total"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "uid" ,property = "user" , javaType = User.class,
                one = @One(select = "com.example.java.mapper.UserMapper.selectById")
            )
    })
    List<Order> selectAllOrderAndUser();
}
