package com.example.java.entity.permission;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 名称
 * @since 2022-12-25
 */
@Data
public class Rolebuttons implements Serializable {

    //    @ApiModelProperty(value = "菜单的按钮编号")
//    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 关联ID，角色属于菜单ID
     */
    private Long roleMenuId;
    /**
     * 按钮中文名称
     */
    private String buttonName;

    /**
     * 按钮英文类型便于匹配是否有权限
     */
    private String buttonType;

    // 角色的ID 便于查询
    private Integer roleId;

    // 菜单真实ID
    private Integer menuId;

}
