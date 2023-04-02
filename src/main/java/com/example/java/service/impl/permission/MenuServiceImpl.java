package com.example.java.service.impl.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.News;
import com.example.java.entity.User;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.Menubutton;
import com.example.java.entity.permission.RoleMenus;
import com.example.java.entity.permission.Rolebuttons;
import com.example.java.mapper.permission.MenuMapper;
import com.example.java.mapper.permission.MenubuttonMapper;
import com.example.java.mapper.permission.RoleButtonsMapper;
import com.example.java.mapper.permission.RolemenusMapper;
import com.example.java.service.permission.IMenuService;
import com.example.java.vo.menus.BatchMenuVo;
import com.example.java.vo.menus.MenusVo;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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

    @Autowired
    private RolemenusMapper rolemenusMapper;

    @Autowired
    private RoleButtonsMapper roleButtonsMapper;

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
        if (!StringUtils.isBlank(menu.getTitle())) {
            queryWrapper.like("title", menu.getTitle());
        }
        queryWrapper.orderByDesc("menuOrder");
        queryWrapper.orderByAsc("id");

        Page<Menu> page = new Page<>(currentPage, pageSize);
        IPage<Menu> menuIPage = menuMapper.selectPage(page, queryWrapper);
        if (menuIPage.getRecords().size() != 0) {
            // 赋值子菜单
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

    /**
     * 获取子菜单
     *
     * @param menus
     * @return
     */
    public List<Menu> getChildrenMenu(List<Menu> menus) {
        List<Integer> ids = menus.stream().map(i -> {
            return i.getId();
        }).collect(Collectors.toList());

        Map<Integer, Menu> map = menus.stream().collect(Collectors.toMap(i -> i.getId(), Function.identity()));

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("fid", ids).orderByDesc("menuOrder");
        List<Menu> menuList = menuMapper.selectList(queryWrapper);
        // 获取二级菜单下边的所有按钮
        List<Menubutton> buttonArr = this.getMenuButtonList(menuList.stream().map(i -> i.getId()).collect(Collectors.toList()));
        // 给二级菜单一级菜单组合
        menuList.forEach(item -> {
            if (map.containsKey(item.getFid())) {
                Menu menu = map.get(item.getFid());
                item.setFname(menu.getName());
                item.setFtitle(menu.getTitle());
                // 匹配出来二级菜单按钮
                List<Menubutton> btns = buttonArr.stream().filter(i -> i.getMenuId().equals(item.getId())).collect(Collectors.toList());
                item.setMenubuttonList(btns);
            }
        });

        return menuList;
    }

    /**
     * 获取二级菜单下的按钮
     *
     * @return
     */
    public List<Menubutton> getMenuButtonList(List<Integer> idArray) {
        List<Menubutton> list = new ArrayList<>();
        list = menubuttonMapper.selectList(new QueryWrapper<Menubutton>().in("menuId", idArray));
        return list;
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


    /**
     * 获取角色的菜单以及菜单对应按钮
     *
     * @param roleId
     */
    public List<Menu> getRoleMenuButtons(User user) {
        // 1 通过角色ID查询出来 角色拥有的菜单
        List<RoleMenus> roleMenusList = rolemenusMapper.selectList(new QueryWrapper<RoleMenus>().eq("roleId", user.getRoleId()));
        // 2 角色菜单查询出 角色拥有按钮
        List<Rolebuttons> rolebuttonsList = roleButtonsMapper.selectList(new QueryWrapper<Rolebuttons>().eq("roleId", user.getRoleId()));
        // 3， 通过角色菜单查询出来真实菜单
        List<Integer> menuIds = roleMenusList.stream().map(i -> i.getMenuId()).collect(Collectors.toList()); // 讲角色的菜单ID转换数组进行查询
        List<Menu> menuList = new ArrayList<>();
        if (menuIds.size() != 0) {
            menuList = menuMapper.selectList(new QueryWrapper<Menu>().in("id", menuIds).orderByDesc("menuOrder"));
        }

        // 4 真是菜单匹配角色菜单按钮
        for (int i = 0; i < menuList.size(); i++) {
            Integer menuId = menuList.get(i).getId();
            List<Menubutton> menubuttonList = rolebuttonsList.stream().filter(item -> {
                return item.getMenuId().equals(menuId);
            }).map(item -> {
                Menubutton menubutton = new Menubutton();
                menubutton.setType(item.getButtonType());
                menubutton.setName(item.getButtonName());
                return menubutton;
            }).collect(Collectors.toList());
            menuList.get(i).setMenubuttonList(menubuttonList);
        }
        System.out.println(menuList);
        return menuList;
    }


    /**
     * 展开前端传递菜单
     *
     * @param batchMenuVoList
     */
    public List<Menu> expandMenu(List<BatchMenuVo> batchMenuVoList) {
        List<Menu> postMenuList = new ArrayList<>();
        for (int i = 0; i < batchMenuVoList.size(); i++) {
            Menu menu = this.setupMenu(batchMenuVoList.get(i));
            if (batchMenuVoList.get(i).getChildren().size() != 0) {
                List<BatchMenuVo> batchMenuList = batchMenuVoList.get(i).getChildren();
                for (BatchMenuVo item : batchMenuList) {
                    Menu subMenu = this.setupMenu(item);
                    postMenuList.add(subMenu);
                }
            }
            postMenuList.add(menu);
        }
        return postMenuList;
    }

    /**
     * 设置菜单
     *
     * @param vo
     * @return
     */
    public Menu setupMenu(BatchMenuVo vo) {
        Menu menu = new Menu();
        menu.setTitle(vo.getTitle());
        menu.setName(vo.getName());
        menu.setMenuOrder(vo.getMeta().get("menuOrder") == null ? 0 : (int) vo.getMeta().get("menuOrder"));
        menu.setIcon((String) vo.getMeta().get("icon"));
        return menu;
    }


    /**
     * 匹配所有菜单
     *
     * @return
     */
    public List<Menu> getAllMenusAndButtons() {
        List<Menu> allMenus = this.list(null);
        // 1 查出来一级菜单
        List<Menu> dataMenu = allMenus.stream().filter(i -> i.getFid().equals(0)).collect(Collectors.toList());
        for (Menu menu : dataMenu) {
            List<Menu> secondaryMenu = allMenus.stream().filter(i -> i.getFid().equals(menu.getId())).collect(Collectors.toList());
            menu.setChildren(secondaryMenu);
        }
        return dataMenu;
    }

}

