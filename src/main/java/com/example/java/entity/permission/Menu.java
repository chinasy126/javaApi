package com.example.java.entity.permission;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 名称
 * @since 2022-12-11
 */
@Data
public class Menu implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 菜单name
     */
    @ApiModelProperty(value = "菜单name")
    private String name;

    private Integer menuOrder;
    private String icon;

    /**
     * 是否二级菜单，父级菜单ID
     */
    @ApiModelProperty(value = "父ID")
    private Integer fid;

    @ApiModelProperty(value = "父级菜单name")
    @TableField(exist = false)
    private String fname;

    @ApiModelProperty(value = "父级菜单名称")
    @TableField(exist = false)
    private String ftitle;

    private String button;

    @TableField(exist = false)
    private List<Menubutton> menubuttonList;

    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)
    private List<Menu> children;

}
