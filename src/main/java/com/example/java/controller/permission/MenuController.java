package com.example.java.controller.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.Menubutton;
import com.example.java.mapper.permission.MenuMapper;
import com.example.java.mapper.permission.MenubuttonMapper;
import com.example.java.service.permission.IMenuService;
import com.example.java.service.permission.IMenubuttonService;
import com.example.java.utils.Result;
import com.example.java.vo.menus.BatchMenuVo;
import com.example.java.vo.menus.MenusVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
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
        //log.info("ccccccccccccccccccccccccccccccccccccccccccccccccc");
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

        // 如果不为空添加二级菜单，为空添加一级菜单
        if (!StringUtils.isBlank(menusVo.getParentMenu())) {
            String[] stringList = menusVo.getParentMenu().split(",");

            QueryWrapper<Menu> menusVoQueryWrapper = new QueryWrapper<>();
            menusVoQueryWrapper.eq("name", stringList[1]).eq("fid", 0);
            Menu menu = menuMapper.selectOne(menusVoQueryWrapper);
            data.setFid(menu.getId());
        } else {
            data.setFid(0);
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
        if(deleteIds.size() > 0){
            int batchDeleteId = menuMapper.deleteBatchIds(deleteIds);
            return Result.ok().data("ok", batchDeleteId);
        }else {
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
     * 批量添加菜单
     *
     * @param batchMenuVoList
     * @return
     */
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public Result batchInsert(@RequestBody List<BatchMenuVo> batchMenuVoList) {

        menuMapper.delete(null); // 删除菜单
        menubuttonMapper.delete(null); // 删除按钮
        // 开始循环前端传递数据[菜单,button 按钮]
        batchMenuVoList.forEach(i -> {
            // 新增一级菜单
            Menu menu = new Menu();
            BeanUtils.copyProperties(i, menu);

            menu.setIcon((String) i.getMeta().get("icon"));
            menu.setMenuOrder((Integer) i.getMeta().get("menuOrder"));

            menuMapper.insert(menu);// 插入一级菜单
            // 新增二级菜单
            if (i.getChildren().size() != 0) {
                int fid = menu.getId(); // 父级菜单
                i.getChildren().forEach(childI -> { // 开始循环二级菜单
                    Menu menuChild = new Menu();
                    BeanUtils.copyProperties(childI, menuChild); // 开始复制内容
                    menuChild.setFid(fid); // 二级菜单添加父级ID
//                    String buttons = StringUtils.join(childI.getButton(), '|');
//                    menuChild.setButton(buttons);
                    menuChild.setIcon((String) childI.getMeta().get("icon"));
                    menuChild.setMenuOrder((Integer) childI.getMeta().get("menuOrder"));


                    menuMapper.insert(menuChild); // 插入二级菜单数据
                    int childId = menuChild.getId(); // 获取二级菜单ID
                    List<Menubutton> menubuttonList = childI.getButton().stream().map(btnI -> {
                        // 给按钮开始赋值
                        Menubutton menubutton = new Menubutton();
                        // 将 按钮的 OBJECT 转换成 MAP < key ， value >
                        Map entity = (Map) btnI;
                        String name = entity.get("title").toString();
                        BeanUtils.copyProperties(btnI, menubutton);
                        menubutton.setMenuId(childId);
                        menubutton.setName(entity.get("title").toString());
                        menubutton.setType(entity.get("type").toString());
                        return menubutton;
                    }).collect(Collectors.toList());
                    iMenubuttonService.saveBatch(menubuttonList);
                    System.out.println(menubuttonList);
                });
            }
        });

        return Result.ok().data("data", "ok");
    }

}
