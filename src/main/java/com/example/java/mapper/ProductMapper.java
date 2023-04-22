package com.example.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java.entity.Product;
import com.example.java.entity.Productclass;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 名称
 * @since 2022-12-05
 */
public interface ProductMapper extends BaseMapper<Product> {

    IPage<Product> selectListByPage(
            Page<Product> page,
            @Param("name") String name,
            @Param("currentPage") Integer currentPage,
            @Param("pageSize") Integer pageSize);

//    @Select("select * from productclass where classid = #{pid}")
//    Productclass selectByClassidId(int pid);
//
//    @Select("select * from product")
//    @Results(
//            {
//                    @Result(column = "id", property = "id"),
//                    @Result(column = "pid", property = "pid"),
//                    @Result(column = "name", property = "name"),
//                    @Result(column = "title", property = "title"),
//                    @Result(column = "pid" , property ="productclass" , javaType = List.class,
//                        many = @Many(select = "ProductMapper.java.selectByClassidId")
//                    )
//            }
//    )
//    List<Product> selectProductAndCategory();

}