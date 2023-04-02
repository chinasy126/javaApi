package com.example.java.service.permission;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java.entity.permission.RoleMenus;
import com.example.java.vo.menus.RoleVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 名称
 * @since 2022-12-15
 */
public interface IRolemenusService extends IService<RoleMenus> {

     public void comparisonarray(RoleVo roleVo);

//    IPage<Role> getDataByPage();

     void rolePower(RoleVo roleVo);

}
