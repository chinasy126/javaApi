package com.example.java.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.RoleMenus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@TableName("t_user")
public class User {
    @ApiModelProperty(value = "用户ID自增")
    @TableId(type = IdType.AUTO)
    private int id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "出生年月")
    private String birthday;

    @ApiModelProperty(value = "所属角色ID")
    private Integer roleId;

    @TableField(exist = false)
    private List<Order> orders;

    @ApiModelProperty(value = "角色名称")
    @TableField(exist = false)
    private String roleName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户菜单列表")
    private List<Menu> menuList;

    @ApiModelProperty(value = "用户头像")
    private String avatar;
}
