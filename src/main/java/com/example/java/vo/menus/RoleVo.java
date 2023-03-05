package com.example.java.vo.menus;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.java.entity.permission.RoleMenus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleVo implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 角色权限
     */
    private List<RoleMenus>  roleMenus;
}
