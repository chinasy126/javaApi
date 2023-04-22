package com.example.java.vo.menus;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


@Data
@TableName("t_user")
public class UserVo {

    private int id;
    private String username;
    private String password;
    private String code;


}
