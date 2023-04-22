package com.example.java.service.permission;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.Menubutton;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 名称
 * @since 2022-12-23
 */
public interface IMenubuttonService extends IService<Menubutton> {

    /**
     * 新增按钮
     * @param menubuttonList
     * @param menuId
     */
    void addChilerenMenuButton(List<Object> menubuttonList, Integer menuId);

    /**
     * 传入一级菜单ID删除二级菜单按钮
     * @param menuId
     */
    void deleteByMenuIds(Integer menuId);

    void compareButton(List<Menubutton> postBtns,List<Menubutton> dataBaseBtns,Integer menuId);

    List<Menubutton> getAllButtons();
}
