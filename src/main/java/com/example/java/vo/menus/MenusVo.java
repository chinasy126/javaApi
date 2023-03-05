package com.example.java.vo.menus;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.java.entity.permission.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenusVo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 菜单name
     */
    private String name;

    /**
     * 是否二级菜单，父级菜单ID
     */
    private Integer fid;

    private List<Menu> children = new ArrayList<>();


    @ApiModelProperty(value = "一级菜单")
    @TableField(exist = false)
    private String parentMenu;

    @ApiModelProperty(value = "菜单Entity")
    Menu menu;
}
