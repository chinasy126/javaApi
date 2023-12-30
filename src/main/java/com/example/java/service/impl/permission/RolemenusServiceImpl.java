package com.example.java.service.impl.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.permission.*;
import com.example.java.mapper.permission.RoleButtonsMapper;
import com.example.java.mapper.permission.RoleMapper;
import com.example.java.mapper.permission.RolemenusMapper;
import com.example.java.service.INewsService;
import com.example.java.service.permission.IRolemenusService;
import com.example.java.vo.menus.RoleVo;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 名称
 * @since 2022-12-15
 */
@Service
public class RolemenusServiceImpl extends ServiceImpl<RolemenusMapper, RoleMenus> implements IRolemenusService {

    @Autowired
    @Resource
    private RolemenusMapper rolemenusMapper;

    @Autowired
    private RoleButtonsMapper roleButtonsMapper;

    @Autowired
    private RoleMapper roleMapper;

    public void comparisonarray(RoleVo roleVo) {

        // 查询当前角色有哪些菜单
        QueryWrapper<RoleMenus> rolemenusQueryWrapper = new QueryWrapper<>();
        rolemenusQueryWrapper.eq("roleId", roleVo.getId());
        List<RoleMenus> rolemenusList = rolemenusMapper.selectList(rolemenusQueryWrapper);

        // 查询出来角色所有的菜单IDS  < 原有ID >
        List<Integer> oldIds = rolemenusList.stream().map(p -> p.getMenuId()).collect(Collectors.toList());
        System.out.println(oldIds + "原有");


        // 前端新选择的 新选择=>数组 | 前端新选择的菜单变成一个数组
        List<Integer> newIds = roleVo.getRoleMenus() != null ? roleVo.getRoleMenus().stream().map(p -> p.getMenuId()).collect(Collectors.toList()) : new ArrayList<>();
        System.out.println(newIds + "新选择");
        // 新选择=>MAP

        // 先删除  新选择的数组变成 Map
        Map<Integer, Integer> newMap = newIds.parallelStream().collect(Collectors.toMap(
                Function.identity(), Function.identity(),
                (oldData, newData) -> newData
        ));
        List<Integer> deleteId = rolemenusList.parallelStream().filter(str -> {
            return !newMap.containsKey(str.getMenuId());
        }).map(e -> {
            return e.getId();
        }).collect(Collectors.toList()); // 要删除的ID ARRAY  oldIds
        // 删除原有菜单
        if (deleteId.size() != 0) {
            // 删除未选择菜单
            rolemenusMapper.deleteBatchIds(deleteId);
            QueryWrapper<Rolebuttons> queryWrapper = new QueryWrapper<>();
            // 删除未选择按钮
            queryWrapper.in("roleMenuId", deleteId);
            roleButtonsMapper.delete(queryWrapper);
        }

        // 再新增
        Map<Integer, Integer> oldMap = oldIds.stream().collect(Collectors.toMap(
                Function.identity(), Function.identity(),
                (oldData, newData) -> newData
        ));

        List<RoleMenus> insertRolemenus = new ArrayList<>();
        // 获取都有哪些插入的ID
        if (roleVo.getRoleMenus() != null) {
            insertRolemenus = roleVo.getRoleMenus().stream().filter(str -> {
                return !oldMap.containsKey(str.getMenuId());
            }).map(e -> {
                e.setRoleId(roleVo.getId());
                return e;
            }).collect(Collectors.toList());
        }

        // 有一种情况插入的数组为空 里边按钮发生变化
        if (insertRolemenus.size() == 0) {
            // 新选择的按钮结构
            this.buttoPermissions(roleVo, rolemenusList);
        }

        if (insertRolemenus.size() > 0) {
            for (int i = 0; i < insertRolemenus.size(); i++) {
                RoleMenus rolemenus = new RoleMenus();
                long insertId = rolemenusMapper.insert(insertRolemenus.get(i));
                List<Rolebuttons> menubuttonList = insertRolemenus.get(i).getRolebuttonsList();
                long btnInsertIntoId = insertRolemenus.get(i).getId();
                // 需要新增的按钮 Buttons
                if (menubuttonList.size() > 0) {
                    for (int j = 0; j < menubuttonList.size(); j++) {
                        Rolebuttons roleBtn = new Rolebuttons();
                        roleBtn.setButtonType(menubuttonList.get(j).getButtonType());
                        roleBtn.setButtonName(menubuttonList.get(j).getButtonName());
                        // 按钮中要有角色ID
                        roleBtn.setRoleId(roleVo.getId());
                        //BeanUtils.copyProperties(menubuttonList.get(j), roleBtn);
                        roleBtn.setRoleMenuId(btnInsertIntoId);
                        roleBtn.setMenuId(insertRolemenus.get(i).getMenuId());
                        roleButtonsMapper.insert(roleBtn);
                    }
                }
            }
        }
    }

    private void buttoPermissions(RoleVo roleVo, List<RoleMenus> rolemenusList) {
        List<Integer> oldMenuIsArr = rolemenusList.stream().map(i -> i.getId()).collect(Collectors.toList());
        List<Rolebuttons> list = roleButtonsMapper.selectList(new QueryWrapper<Rolebuttons>().in("roleMenuId", oldMenuIsArr));
        // 方案一
        // 1.先删除所有按钮

        roleButtonsMapper.delete(new QueryWrapper<Rolebuttons>().in("roleMenuId", oldMenuIsArr));
        // 2.新增所有按钮
        for (Integer i = 0; i < roleVo.getRoleMenus().size(); i++) {
            if (roleVo.getRoleMenus().get(i).getRolebuttonsList().size() != 0) {
                RoleMenus roleMeu = rolemenusMapper.selectOne(new QueryWrapper<RoleMenus>()
                        .eq("roleId", roleVo.getId())
                        .eq("menuId", roleVo.getRoleMenus().get(i).getMenuId()));
                List<Rolebuttons> rolebuttons = roleVo.getRoleMenus().get(i).getRolebuttonsList();
                for (Integer y = 0; y < rolebuttons.size(); y++) {
                    Rolebuttons roleBtn = new Rolebuttons();
                    roleBtn.setRoleMenuId(Long.valueOf(roleMeu.getId()));
                    roleBtn.setButtonName(rolebuttons.get(y).getButtonName());
                    roleBtn.setButtonType(rolebuttons.get(y).getButtonType());
                    roleBtn.setRoleId(roleVo.getId());
                    roleBtn.setMenuId(roleMeu.getMenuId());
                    roleButtonsMapper.insert(roleBtn);
                }
            }
        }
        // ****************************************************************

        // 1 查询原有按钮
//        List<Integer> oldMenuIsArr = rolemenusList.stream().map(i -> i.getId()).collect(Collectors.toList());
//        List<Rolebuttons> list = roleButtonsMapper.selectList(new QueryWrapper<Rolebuttons>().in("roleMenuId", oldMenuIsArr));
        // 2 与现在按钮对比

        // 前端传递过来的按钮列表
//        List<Rolebuttons> roleBtnList = new ArrayList<>();
//        for (Integer i = 0; i < roleVo.getRoleMenus().size(); i++) {
//            // 判断如果是二级分类下边有按钮
//            if (roleVo.getRoleMenus().get(i).getRolebuttonsList().size() != 0) {
//                List<Rolebuttons> btnList = roleVo.getRoleMenus().get(i).getRolebuttonsList();
//                for (Integer y = 0; y < btnList.size(); y++) {
//                    // System.out.println(btnList.get(y));
//                    roleBtnList.add(btnList.get(y));
//                    // roleMenusList.add(btnList.get(y));
//                }
//            }
//        }
//        System.out.println(roleBtnList);
//        List<Integer> delIdArr = new ArrayList<>();
//        for (Integer i = 0; i < list.size(); i++) {
//            for (Integer y = 0; y < roleBtnList.size(); y++) {
//                if (list.get(0).equals(roleBtnList.get(y))) {
//                }
//                break;
//            }
//        }

        // 3 删除原有按钮

        // 4新增现有按钮
    }

    @Autowired
    IRolemenusService iRolemenusService;

    /**
     * 角色管理中操作菜单按钮功能
     *
     * @param roleVo
     */
    public void rolePower(RoleVo roleVo) {
        // 1 无论新增还是修改第一步清空改用户下所有菜单，按钮
        System.out.println(roleVo);
        Integer roleId = roleVo.getId();
        // 清空按钮
        roleButtonsMapper.delete(new QueryWrapper<Rolebuttons>().eq("roleId", roleId));
        // 清空菜单
        rolemenusMapper.delete(new QueryWrapper<RoleMenus>().eq("roleId", roleId));
        // 2 新增菜单
        List<RoleMenus> roleMenusList = roleVo.getRoleMenus();
        for (int i = 0; i < roleMenusList.size(); i++) {
            RoleMenus roleMenus = new RoleMenus();
            roleMenus.setRoleId(roleId); // 角色ID
            roleMenus.setMenuId(roleMenusList.get(i).getMenuId()); // 真实菜单ID
            roleMenus.setMenuTitle(roleMenusList.get(i).getMenuTitle()); // 真实菜单名称
            rolemenusMapper.insert(roleMenus);
            if (roleMenusList.get(i).getRolebuttonsList().size() != 0) {
                List<Rolebuttons> rolebuttons = roleMenusList.get(i).getRolebuttonsList();
                this.addButtons(rolebuttons, roleVo, roleMenus);
            }
        }
        // 3 新增按钮

        Role roleUpdate = new Role();
        roleUpdate.setId(roleVo.getId());
        roleUpdate.setRoleName(roleVo.getRoleName());
        roleUpdate.setRoleDesc(roleVo.getRoleDesc());
        roleMapper.updateById(roleUpdate);

    }

    /**
     * 新增菜单下边的按钮
     *
     * @param roleMenus
     * @param roleVo
     * @param rMenu
     */
    private void addButtons(List<Rolebuttons> roleMenus, RoleVo roleVo, RoleMenus rMenu) {
        List<Rolebuttons> rolebuttonsList = new ArrayList<>();
        for (int i = 0; i < roleMenus.size(); i++) {
            // 获取菜单下的按钮
            Rolebuttons rolebuttons = new Rolebuttons();
            BeanUtils.copyProperties(roleMenus.get(i), rolebuttons);
            rolebuttons.setRoleMenuId((long) rMenu.getId());
            rolebuttons.setRoleId(rMenu.getRoleId());
            rolebuttons.setMenuId(rMenu.getMenuId());
            roleButtonsMapper.insert(rolebuttons);
        }
    }

}