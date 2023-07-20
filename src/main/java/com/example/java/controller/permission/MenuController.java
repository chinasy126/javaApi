package com.example.java.controller.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.java.entity.Productclass;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.Menubutton;
import com.example.java.mapper.permission.MenuMapper;
import com.example.java.mapper.permission.MenubuttonMapper;
import com.example.java.service.permission.IMenuService;
import com.example.java.service.permission.IMenubuttonService;
import com.example.java.utils.Result;
import com.example.java.vo.menus.BatchMenuVo;
import com.example.java.vo.menus.MenusVo;
import io.swagger.models.auth.In;
import javafx.scene.input.Mnemonic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 名称
 * @since 2022-12-11
 */
@RestController
@CrossOrigin
@RequestMapping("/menu")
@Slf4j
public class MenuController {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    IMenuService iMenuService;

    @Autowired
    MenubuttonMapper menubuttonMapper;

    @Autowired
    IMenubuttonService iMenubuttonService;

    /**
     * 数据列表
     *
     * @param productClass
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result dataList(@RequestBody Menu menu, @RequestParam int currentPage, @RequestParam int pageSize) {
        IPage<Menu> iPage = iMenuService.getDataByPage(currentPage, pageSize, menu);
        return Result.ok().data("data", iPage);
    }

    @RequestMapping(value = "/menulist", method = RequestMethod.POST)
    public Result menuList(@RequestBody Menu menu, @RequestParam int currentPage, @RequestParam int pageSize) {
        IPage<Menu> menuList = iMenuService.getMenuList(currentPage, pageSize, menu);
        return Result.ok().data("data", menuList);
    }

    /**
     * 插入数据
     *
     * @param productclass
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result dataInsert(@RequestBody Menu menu) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper();
        int insertId = menuMapper.insert(menu);
        return Result.ok().data("insertId", insertId);
    }


    /**
     * 修改数据
     *
     * @param productclass
     * @param request
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Result dataModify(@RequestBody Menu menu, HttpServletRequest request) {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        if (menu.getId() != null) {
            menuQueryWrapper.eq("id", menu.getId());
            int updateId = menuMapper.update(menu, menuQueryWrapper);
            return Result.ok().data("data", updateId);
        } else {
            int insertId = menuMapper.insert(menu);
            return Result.ok().data("data", insertId);
        }
    }

    /**
     * 新增与修改
     *
     * @param menu
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result insertOrUpdate(@RequestBody MenusVo menusVo) {
        Menu data = new Menu();
        BeanUtils.copyProperties(menusVo, data);


<<<<<<< HEAD
        // 如果不为空添加二级菜单， !StringUtils.isBlank(menusVo.getParentMenu())
        if (menusVo.getParentMenu() != null) {
=======
        if (menusVo.getChildren() != null && menusVo.getChildren().size() > 0 &&
                menusVo.getParentMenu() != null
                && !menusVo.getParentMenu().trim().isEmpty()) {
            return Result.error().message("无法移动子菜单的一级菜单");
        }

        if (menusVo.getId() != null) {
            int fid = menuMapper.selectOne(new QueryWrapper<Menu>().eq("id", menusVo.getId())).getFid();
            if (fid != menusVo.getFid()) {
                return Result.error().message("移动请慢一些，请正常移动");
            }
        }

        // 如果不为空添加二级菜单， !StringUtils.isBlank(menusVo.getParentMenu())
        if (menusVo.getParentMenu() != null && menusVo.getParentMenu().length() != 0 && menusVo.getParentMenu() != "") {
>>>>>>> master
            Menu menu = iMenuService.getOne(new QueryWrapper<Menu>().eq("name", menusVo.getParentMenu().split(",")[1]));
//            String[] stringList = menusVo.getParentMenu().split(",");
//            String[] stringList = new String[0];
//            QueryWrapper<Menu> menusVoQueryWrapper = new QueryWrapper<>();
//            menusVoQueryWrapper.eq("name", stringList[1]).eq("fid", 0);
//            Menu menu = menuMapper.selectOne(menusVoQueryWrapper);
//            data.setFid(menu.getId());
            data.setFid(menu.getId());
        } else {
<<<<<<< HEAD
            data.setFid(0);
=======
            // data.setFid(0);
>>>>>>> master
        }
        Boolean menuBoolean = iMenuService.saveOrUpdate(data);
        return Result.ok().data("ok", menuBoolean);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
//    public Result dataDelete(@PathVariable int id) {
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result dataDelete(@RequestBody Menu menu) {

        // 删除菜单下对应按钮
        // 如果删除的是一级菜单 先查询二级菜单，后删除二级菜单对应按钮
        iMenubuttonService.deleteByMenuIds(menu.getId());

        // 先删除按钮 后删除菜单
        QueryWrapper<Menu> newsQueryWrapper = new QueryWrapper<Menu>();
        newsQueryWrapper.eq("id", menu.getId());
        int deleteId = menuMapper.delete(newsQueryWrapper);

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fid", menu.getId());
        menuMapper.delete(queryWrapper);

        return Result.ok().data("deleteId", deleteId);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/batchdelete", method = RequestMethod.DELETE)
    public Result batchDelete(@RequestBody Map<String, List<Integer>> map) {
        List<Integer> deleteIds = map.get("ids");
        if (deleteIds.size() > 0) {
            for (Integer id : deleteIds) {
                iMenubuttonService.deleteByMenuIds(id);
            }
            int batchDeleteId = menuMapper.deleteBatchIds(deleteIds);
            return Result.ok().data("ok", batchDeleteId);
        } else {
            return Result.error().message("请选择要删除数据");
        }

    }

    /**
     * 获取所有分类
     *
     * @return
     */
    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public Result getClassList() {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fid", 0);
        queryWrapper.orderByAsc("id");
        List<Menu> list = menuMapper.selectList(queryWrapper);
        return Result.ok().data("data", list);
    }


    /**
     * 获取级联的菜单所有内容
     */
    @RequestMapping(value = "/multiclassclassification", method = RequestMethod.POST)
    public Result getMultilevelClassification() {
        List<MenusVo> menusVoList = iMenuService.getAllMultilevelClassification();
        return Result.ok().data("data", menusVoList);
    }

    /**
     * 转换拼接 菜单与按钮
     *
     * @param menuList
     * @param menubuttonList
     * @return
     */

    public List<Menu> getMenuAndBtn(List<Menu> menuList, List<Menubutton> menubuttonList) {
        for (int i = 0; i < menuList.size(); i++) {
            for (Menu menu : menuList.get(i).getChildren()) {
                List<Menubutton> menuBtnList = menubuttonList.stream().filter(item -> item.getMenuId() == menu.getId()).collect(Collectors.toList());
                menu.setMenubuttonList(menuBtnList);
            }
        }
        return menuList;
    }

    /**
     * 批量添加菜单
     *
     * @param batchMenuVoList
     * @return
     */
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public Result batchInsert(@RequestBody List<BatchMenuVo> batchMenuVoList) {
        // 数据库获取前端菜单
        List<Menu> AllMenu = iMenuService.getAllMenusAndButtons();
        // 数据库获取所有按钮
        List<Menubutton> AllButton = iMenubuttonService.getAllButtons();
        // 转换带按钮的菜单
        List<Menu> databaseMenu = this.getMenuAndBtn(AllMenu, AllButton);

        // 1 展开前端菜单
        List<Menu> postMenuList = iMenuService.expandMenu(batchMenuVoList);
        // 2 前端菜单循环 新增
        for (BatchMenuVo menu : batchMenuVoList) {
            Menu m = databaseMenu.stream().filter(i -> i.getName().equals(menu.getName())).findAny().orElse(null);
            if (m != null) {
                // 存在一级菜单
                for (BatchMenuVo subPostMenu : menu.getChildren()) {
                    List<Menu> subDatabaseMenu = m.getChildren();
                    Menu subMenu = subDatabaseMenu.stream().filter(i -> i.getName().equals(subPostMenu.getName())).findAny().orElse(null);
                    if (subMenu == null) {
                        // 如果数据库中不存在二级菜单，则新增
                        Menu insertMent = new Menu();
                        BeanUtils.copyProperties(subPostMenu, insertMent);
                        insertMent.setFid(m.getId());
                        // 新增二级菜单
                        menuMapper.insert(insertMent);
                        // 新增二级菜单按钮
                        iMenubuttonService.addChilerenMenuButton(subPostMenu.getButton(), insertMent.getId());
                    } else {
                        // 如果二级菜单有则对比按钮 比对按钮
                        List<Menubutton> menubuttonList = subPostMenu.getButton().stream()
                                .map(btn -> {
                                    Map entity = (Map) btn;
                                    Menubutton menubutton = new Menubutton();
                                    menubutton.setName(entity.get("title").toString());
                                    menubutton.setType(entity.get("type").toString());
                                    return menubutton;
                                }).collect(Collectors.toList());
                        // 对比二级菜单 前端传递，数据库查询的按钮
                        iMenubuttonService.compareButton(menubuttonList, subMenu.getMenubuttonList(), subMenu.getId());
                    }
                }
            } else {
                // 数据库中不存在一级菜单新增
                Menu insertMainMenu = new Menu();
                BeanUtils.copyProperties(menu, insertMainMenu);
                // 新增一级菜单
                menuMapper.insert(insertMainMenu);
                menu.getChildren().forEach(item -> {
                    Menu insertSubMenu = new Menu();
                    BeanUtils.copyProperties(item, insertSubMenu);
                    insertSubMenu.setFid(insertMainMenu.getId());
                    // 新增二级菜单
                    menuMapper.insert(insertSubMenu);
                    // 去添加二级菜单的按钮
                    iMenubuttonService.addChilerenMenuButton(item.getButton(), insertSubMenu.getId());
                });
            }
        }

        // 3 数据库中菜单循环
        for (Menu menu : databaseMenu) {
            BatchMenuVo m = batchMenuVoList.stream()
                    .filter(i -> i.getName().equals(menu.getName()))
                    .findAny()
                    .orElse(null);
            // 数据库中的菜单多了，则删除，
            // 一级菜单
            if (m == null) {
                // 删除方法

                // 删除二级菜单按钮
                iMenubuttonService.deleteByMenuIds(menu.getId());
                // 删除一级菜单
                menuMapper.delete(new QueryWrapper<Menu>().eq("id", menu.getId()));
                // 删除二级菜单
                menuMapper.delete(new QueryWrapper<Menu>().eq("fid", menu.getId()));
            } else {
                // 一级菜单对比没有问题，对比二级菜单
                for (Menu subMenu : menu.getChildren()) {
                    BatchMenuVo postSubM = m.getChildren().stream()
                            .filter(i -> i.getName().equals(subMenu.getName()))
                            .findAny().orElse(null);
                    if (postSubM == null) {
                        menubuttonMapper.delete(new QueryWrapper<Menubutton>().eq("menuId", subMenu.getId()));
                        // 二级菜单中数据库存在，前端不存在则删除
                        iMenuService.removeById(subMenu.getId());
                        //  menuMapper.delete(new QueryWrapper<Menu>().eq("id", subMenu.getId()));
                    }
                }

            }

        }
        return Result.ok().data("data", "ok");
    }


    /**
     * 通过一级菜单获取所有二级菜单
     *
     * @param menu
     * @return
     */
    @RequestMapping(value = "/getSecMenuList", method = RequestMethod.POST)
    public Result getSecMenuList(@RequestBody Menu menu) {
        List<Menu> menus = menuMapper.selectList(new QueryWrapper<Menu>().eq("fid", menu.getId()));
        return Result.ok().data("data", menus);
    }

}
