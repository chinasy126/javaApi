package com.example.java.service.impl.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.permission.Menubutton;
import com.example.java.entity.permission.Rolebuttons;
import com.example.java.entity.permission.RoleMenus;
import com.example.java.mapper.permission.RoleButtonsMapper;
import com.example.java.mapper.permission.RolemenusMapper;
import com.example.java.service.INewsService;
import com.example.java.service.permission.IRolemenusService;
import com.example.java.vo.menus.RoleVo;
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

    public void comparisonarray(RoleVo roleVo) {

        // 查询当前角色有哪些菜单
        QueryWrapper<RoleMenus> rolemenusQueryWrapper = new QueryWrapper<>();
        rolemenusQueryWrapper.eq("roleId", roleVo.getId());
        List<RoleMenus> rolemenusList = rolemenusMapper.selectList(rolemenusQueryWrapper);

        // 查询出来角色 所有的IDS  < 原有ID >
        List<Integer> oldIds = rolemenusList.stream().map(p -> p.getMenuId()).collect(Collectors.toList());
        System.out.println(oldIds + "原有");


        // 前端新选择的 新选择=>数组
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
        
        if(roleVo.getRoleMenus() != null){
            insertRolemenus = roleVo.getRoleMenus().stream().filter(str -> {
                return !oldMap.containsKey(str.getMenuId());
            }).map(e -> {
                e.setRoleId(roleVo.getId());
                return e;
            }).collect(Collectors.toList());
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
                        //BeanUtils.copyProperties(menubuttonList.get(j), roleBtn);
                        roleBtn.setRoleMenuId(btnInsertIntoId);
                        roleButtonsMapper.insert(roleBtn);
                    }
                }
            }
        }
    }


//    public boolean saveBatch(){
//        this.saveBatch()
//    }

//    public IPage<Menu> getDataByPage(int currentPage, int pageSize, Menu menu) {
//
//        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        int current, size;
//        size = pageSize;
//        current = currentPage;
//        Page<Menu> page = new Page<>(current, size);
//        IPage<Menu> newsIPage = menuMapper.selectPage(page, queryWrapper);
//        return newsIPage;
//    }

}
