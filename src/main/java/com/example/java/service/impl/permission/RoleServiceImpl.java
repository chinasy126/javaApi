package com.example.java.service.impl.permission;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.permission.Role;
import com.example.java.entity.permission.RoleMenus;
import com.example.java.mapper.permission.RoleMapper;
import com.example.java.mapper.permission.RolemenusMapper;
import com.example.java.service.permission.IRoleService;
import com.example.java.vo.menus.RoleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    @Resource
    private RoleMapper roleMapper;

    @Autowired
    @Resource
    private RolemenusMapper rolemenusMapper;


    public IPage<Role> getDataByPage(int currentPage, int pageSize, Role role) {
        int current, size;
        size = pageSize;
        current = currentPage;

        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<Role> page = new Page<>(current, size);
        IPage<Role> newsIPage = roleMapper.selectPage(page, queryWrapper);



//        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery(Role.class);
//        List<Role> roleList = this.list(wrapper);
//        System.out.println(roleList+"!!!!!!!!!!!!!");
//        List<RoleVo> roleVoList = new ArrayList<>();
//        BeanUtils.copyProperties(roleVoList,roleList);
//
//        System.out.println(roleVoList+"@@@@@@@@@@@@@@@@@@");


//        QueryWrapper<RoleVo> roleVoQueryWrapper = new QueryWrapper<>();
//        roleVoQueryWrapper.orderByDesc("id");
//        Page<RoleVo> roleVoPage = new Page<>(current,size);

//        IPage<RoleVo> roleVoIPage = new Page<>();
//        BeanUtils.copyProperties(roleVoIPage, newsIPage);
//        newsIPage.getRecords().get(0).setRoleMenus("asdfasdfasdfasdfasdfasdfasdf");
//

      //  LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery(Role.class).eq(Role::getId)

//        for (int i = 0; i < newsIPage.getRecords().size(); i++) {
//            int roleId = newsIPage.getRecords().get(0).getId();
//            QueryWrapper<RoleMenus> rolemenusQueryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("roleId", roleId);
//            System.out.println(rolemenusMapper.selectList(rolemenusQueryWrapper));
//        }

      //  System.out.println(newsIPage.getRecords() + "&&&&&&&&&&&&&&&&&&&");



        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery(Role.class)
                .eq(Role::getId, 10);
        // 先查询用户信息
        Role user = roleMapper.selectOne(wrapper);
        // 转化为Vo
      // RoleVo userVo = Optional.ofNullable(user).orElse(null);
        // 从其它表查询信息再封装到Vo
      //  Optional.ofNullable(userVo).ifPresent(this::addDetpNameInfo);

        return newsIPage;
    }
}
