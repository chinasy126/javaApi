package com.example.java.mapper.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java.entity.Product;
import com.example.java.entity.Productclass;
import com.example.java.entity.permission.Rolebuttons;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoleButtonsMapper extends BaseMapper<Rolebuttons> {

//        @Select("select * from productclass where classid = #{pid}")
//        Productclass selectByClassidId(int pid);

        @Delete("DELETE FROM rolebuttons WHERE roleMenuId IN (${roleMenuIds})")
        Integer deleteBatchByRoleMenuIds(String roleMenuIds);
}
