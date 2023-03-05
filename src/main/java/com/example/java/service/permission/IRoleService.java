package com.example.java.service.permission;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.Role;

public interface IRoleService extends IService<Role> {

    IPage<Role> getDataByPage(int currentPage, int pageSize, Role role);
}
