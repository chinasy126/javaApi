package com.example.java.mapper.permission;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java.entity.Product;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.Role;
import com.example.java.vo.menus.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 名称
 * @since 2022-12-11
 */
public interface MenuMapper extends BaseMapper<Menu> {

    //    List<RoleVo> selectListByPage( @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    IPage<RoleVo> selectListByPage(@Param("roleVoPage") Page<Role> roleVoPage, @Param("role") Role role);
}
