package com.example.java.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java.entity.User;
import com.example.java.mapper.UserMapper;
import com.example.java.utils.JwtUtils;
import com.example.java.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
//@CrossOrigin
@Api(value = "用户相关信息",description = "获取跟用户相关的内容信息")
public class UseroldController {

    @Autowired
    private UserMapper userMapper;


    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Result info(String token){
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        return Result.ok().data("name",username);
    }

    @GetMapping("/user/findByPage")
    public IPage findBypage(){
        Page<User> page = new Page<>(0,2);
        IPage iPage = userMapper.selectPage(page,null);
        return iPage;
    }

    @RequestMapping(value = "/user/find", method = RequestMethod.GET)
    public List<User> findByCond(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username","123");
        return userMapper.selectList(queryWrapper);
    }

    @RequestMapping(value = "/getuser",method = RequestMethod.GET)
    public List query(){
      // List<User> list = userMapper.selectList(null);
        List<User> list = userMapper.selectAllUserAndOrders();
        System.out.println(list);
        return list;
    }


    @RequestMapping(value = "/saveuser",method = RequestMethod.POST)
    public int saveuser(User user){
        return userMapper.insert(user);
    }

//
//    @RequestMapping("/delete/user")
//    public int deleteUser(int id){
//        return userMapper.delete(id);
//    }



    @ApiOperation(value = "获取用户" ,notes = "根据ID获取用户信息")
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public String getUserById(@PathVariable int id){
        System.out.println(id);
        return "获取id"+id;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String save(User user){
        return "添加用户";
    }

    @RequestMapping(value = "/user" , method = RequestMethod.PUT)
    public String update(User user){
        return "更新用户";
    }

    @RequestMapping(value = "/user/{id}" ,method = RequestMethod.DELETE)
    public String deleteById(@PathVariable int id){
        return  "删除ID"+id;
    }
}
