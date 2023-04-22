package com.example.java.service.impl.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.permission.Menu;
import com.example.java.entity.permission.Menubutton;
import com.example.java.mapper.permission.MenuMapper;
import com.example.java.mapper.permission.MenubuttonMapper;
import com.example.java.service.permission.IMenubuttonService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 名称
 * @since 2022-12-23
 */
@Service
public class MenubuttonServiceImpl extends ServiceImpl<MenubuttonMapper, Menubutton> implements IMenubuttonService {

    @Autowired
    private MenuMapper menuMapper;


    /**
     * 新增按钮
     *
     * @param menubuttonList
     * @param menuId
     */
    public void addChilerenMenuButton(List<Object> menubuttonList, Integer menuId) {
        if (menubuttonList.size() != 0) {

            List<Menubutton> menubuttons = menubuttonList.stream().map(btnI -> {
                // 给按钮开始赋值
                Menubutton menubutton = new Menubutton();
                // 将 按钮的 OBJECT 转换成 MAP < key ， value >
                Map entity = (Map) btnI;
                menubutton.setMenuId(menuId);
                menubutton.setType(entity.get("type").toString());
                menubutton.setName(entity.get("title").toString());
                return menubutton;
            }).collect(Collectors.toList());
            this.saveBatch(menubuttons);
        }
    }

    /**
     * 删除菜单对应按钮
     *
     * @param menuId
     */
    public void deleteByMenuIds(Integer menuId) {
        // 1. 查询出来所有二级菜单ID 2转换二级菜单ID
        List<Integer> idList = menuMapper.selectList(new QueryWrapper<Menu>()
                .eq("fid", menuId)
                .select("id"))
                .stream()
                .map(i -> i.getId())
                .collect(Collectors.toList());
        if (idList.size() != 0) {
            this.remove(new QueryWrapper<Menubutton>().in("menuId", idList));
        }
        this.remove(new QueryWrapper<Menubutton>().in("menuId", menuId));
    }

    /**
     * 对比按钮比较
     *
     * @param postBtns
     * @param dataBaseBtns
     * @param menuId
     */
    public void compareButton(List<Menubutton> postBtns, List<Menubutton> dataBaseBtns, Integer menuId) {
        if (postBtns.size() != 0) {
            // 前端先循环
            for (Menubutton postBtn : postBtns) {
                Menubutton postRes = dataBaseBtns == null ? null : dataBaseBtns.stream().filter(i -> i.getType().equals(postBtn.getType()) && i.getName().equals(postBtn.getName())).findAny().orElse(null);
                if (postRes == null) {
                    postBtn.setMenuId(menuId);
                    this.save(postBtn);
                }
            }
        }

        if (dataBaseBtns != null) {
            // 后端后循环
            for (Menubutton dataBtn : dataBaseBtns) {
                Menubutton result = postBtns.stream().filter(i -> i.getName().equals(dataBtn.getName()) && i.getType().equals(dataBtn.getType())).findAny().orElse(null);
                if (result == null)
                    this.removeById(dataBtn.getId());
            }
        }
    }

    public List<Menubutton> getAllButtons(){
       return this.list(null);
    }
}
