package com.example.java.service.permission;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java.entity.Product;
import com.example.java.entity.permission.Menu;
import com.example.java.vo.menus.MenusVo;

import java.util.List;

/**
 * @author 名称
 * @since 2022-12-11
 */
public interface IMenuService extends IService<Menu> {

    // 分页查询
    IPage<Menu> getDataByPage(int currentPage, int pageSize, Menu menu);

    // 查询树级结构

    IPage<Menu> getMenuList(int currentPage, int pageSize, Menu menu);

    //    List<MenusVo>
    List<MenusVo> getAllMultilevelClassification();

}
