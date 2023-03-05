package com.example.java.service.impl.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.News;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.Menubutton;
import com.example.java.mapper.permission.MenuMapper;
import com.example.java.mapper.permission.MenubuttonMapper;
import com.example.java.service.permission.IMenuService;
import com.example.java.vo.menus.MenusVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 名称
 * @since 2022-12-11
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    @Resource
    private MenuMapper menuMapper;

    @Autowired
    private MenubuttonMapper menubuttonMapper;


    public IPage<Menu> getDataByPage(int currentPage, int pageSize, Menu menu) {

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        int current, size;
        size = pageSize;
        current = currentPage;
        Page<Menu> page = new Page<>(current, size);
        IPage<Menu> newsIPage = menuMapper.selectPage(page, queryWrapper);

        List<Integer> ids = newsIPage.getRecords().stream().filter(i -> {
            return i.getFid() != 0;
        }).map(i -> {
            return i.getFid();
        }).collect(Collectors.toList());
        List<Menu> menus = menuMapper.selectBatchIds(ids);

        newsIPage.getRecords().forEach(item -> {
            System.out.println(menus);
            String fName = menus.stream().filter(i -> {
                return i.getId() == item.getFid();
            }).map(i -> {
                return i.getTitle();
            }).collect(Collectors.joining(","));
            item.setFname(fName);
        });

        return newsIPage;
    }


    /**
     * 查询树级结构
     *
     * @param currentPage
     * @param pageSize
     * @param menu
     * @return
     */
    public IPage<Menu> getMenuList(int currentPage, int pageSize, Menu menu) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fid", 0);
        if(!StringUtils.isBlank(menu.getTitle())){
            queryWrapper.like("title", menu.getTitle());
        }
        queryWrapper.orderByAsc("id");

        Page<Menu> page = new Page<>(currentPage, pageSize);
        IPage<Menu> menuIPage = menuMapper.selectPage(page, queryWrapper);
        if (menuIPage.getRecords().size() != 0) {
            List<Menu> menuChildrenList = this.getChildrenMenu(menuIPage.getRecords());
            menuIPage.getRecords().forEach(item -> {
                List<Menu> childList = menuChildrenList.stream().filter(i -> {
                    return i.getFid().equals(item.getId());
                }).collect(Collectors.toList());
                item.setChildren(childList);
            });
        }

        return menuIPage;
    }

    public List<Menu> getChildrenMenu(List<Menu> menus) {
        System.out.println(menus);
        List<Integer> ids = menus.stream().map(i -> {
            return i.getId();
        }).collect(Collectors.toList());

        Map<Integer, Menu> map = menus.stream().collect(Collectors.toMap(i -> i.getId(), Function.identity()));

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("fid", ids);
        List<Menu> menuList = menuMapper.selectList(queryWrapper);

        menuList.forEach(item -> {
            if (map.containsKey(item.getFid())) {
                Menu menu = map.get(item.getFid());
                item.setFname(menu.getName());
                item.setFtitle(menu.getTitle());
            }
        });

        return menuList;
    }

    /**
     * 获取所有级联的分类
     *
     * @return List<MenusVo>
     */
    public List<MenusVo> getAllMultilevelClassification() {
//        所有一级分类
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fid", 0);
        List<Menu> menuList = menuMapper.selectList(queryWrapper);
// 所有二级分类

        QueryWrapper<Menu> subQueryWrapper = new QueryWrapper<>();
        subQueryWrapper.ne("fid", 0);
        List<Menu> subMenuList = menuMapper.selectList(subQueryWrapper);

        List<Menubutton> menubuttonList = menubuttonMapper.selectList(null);
// 二级菜单按钮


//        创建合集
        // 循环一级菜单
        List<MenusVo> menusVoList = new ArrayList<>();
        for (int i = 0; i < menuList.size(); i++) {
            Menu menu = menuList.get(i);
            MenusVo menusVo = new MenusVo();
            BeanUtils.copyProperties(menu, menusVo);
            // 循环二级菜单
            List<Menu> subMenuList1 = new ArrayList<>();
            for (int j = 0; j < subMenuList.size(); j++) {
                Menu subMenu = subMenuList.get(j);

                // S 二级菜单当如按钮列表
                List<Menubutton> menus = menubuttonList.stream().filter(str -> {
                    return str.getMenuId().equals(subMenu.getId());
                }).collect(Collectors.toList());
                subMenu.setMenubuttonList(menus);
                // E 二级菜单当如按钮列表

                if (menu.getId().equals(subMenu.getFid())) {
                    subMenuList1.add(subMenu);
                }
            }
            // 子菜单 放入一级菜单
            menusVo.setChildren(subMenuList1);
            menusVoList.add(menusVo);

        }
        return menusVoList;

    }

}

