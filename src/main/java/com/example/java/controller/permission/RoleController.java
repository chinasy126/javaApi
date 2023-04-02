package com.example.java.controller.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java.entity.Product;
import com.example.java.entity.permission.Menubutton;
import com.example.java.entity.permission.Role;
import com.example.java.entity.permission.Rolebuttons;
import com.example.java.entity.permission.RoleMenus;
import com.example.java.mapper.permission.*;

import com.example.java.service.permission.IRoleService;
import com.example.java.service.permission.IRolebuttonsService;
import com.example.java.service.permission.IRolemenusService;
import com.example.java.utils.Result;
import com.example.java.vo.menus.RoleVo;
import com.sun.org.apache.xpath.internal.functions.FuncUnparsedEntityURI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolemenusMapper rolemenusMapper;

    @Autowired
    IRoleService iRoleService;

    @Autowired
    IRolemenusService iRolemenusService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenubuttonMapper menubuttonMapper;

    @Autowired
    private RoleButtonsMapper roleButtonsMapper;

    private IRolebuttonsService iRolebuttonsService;

    /**
     * 拷贝数组   BeanCopyUtil.copyListProperties(userDOList, UserVO::new);
     *
     * @param roleVo
     * @return
     */

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insertData(@RequestBody RoleVo roleVo) {

        Role role = new Role();
        BeanUtils.copyProperties(roleVo, role);
        roleMapper.insert(role);
        int insertId = role.getId();
        roleVo.setId(insertId);
        iRolemenusService.rolePower(roleVo);
        return Result.ok().data("data", insertId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result dataList(@RequestBody Role role, @RequestParam int currentPage, @RequestParam int pageSize) {
        Page<Role> page = new Page<>(currentPage, pageSize);
        IPage<RoleVo> roleVoList = menuMapper.selectListByPage(page, role);
        return Result.ok().data("data", roleVoList);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Result modifyData(@RequestBody RoleVo roleVo) {
        iRolemenusService.rolePower(roleVo);
        return Result.ok().data("data", "ok");
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
    public Result dataDelete(@RequestBody Role role) {
        int id = role.getId();
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<Role>();
        roleQueryWrapper.eq("id", id);

        // 删除权限菜单
        QueryWrapper<RoleMenus> rolemenusQueryWrapper = new QueryWrapper<>();
        rolemenusQueryWrapper.eq("roleId", id);
        int roleMenusId = rolemenusMapper.delete(rolemenusQueryWrapper);
        System.out.println(roleMenusId + "roleMenusId");
        // 删除角色表
        int deleteId = roleMapper.delete(roleQueryWrapper);
        return Result.ok().data("deleteId", deleteId);
    }


    /**
     * 获取所有角色
     *
     * @return
     */
    @RequestMapping(value = "/rolelist", method = RequestMethod.POST)
    public Result roleList() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Role> roleList = roleMapper.selectList(queryWrapper);
        return Result.ok().data("data", roleList);
    }

}
