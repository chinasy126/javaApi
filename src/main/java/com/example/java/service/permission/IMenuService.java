package com.example.java.service.permission;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java.entity.Product;
import com.example.java.entity.User;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.Menubutton;
import com.example.java.vo.menus.BatchMenuVo;
import com.example.java.vo.menus.MenusVo;

import java.util.List;
import java.util.Map;

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

    List<Menu> getRoleMenuButtons(User user);


    /**
     * 设置菜单
     * @param vo
     * @return
     */
    Menu setupMenu(BatchMenuVo vo);

    /**
     * 展开前端传递的菜单
     * @param batchMenuVoList
     */
    List<Menu> expandMenu(List<BatchMenuVo> batchMenuVoList);

    List<Menu> getAllMenusAndButtons();
}
