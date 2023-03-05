package com.example.java.controller.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java.entity.User;
import com.example.java.mapper.UserMapper;
import com.example.java.service.permission.IUserService;
import com.example.java.utils.MD5Util;
import com.example.java.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    IUserService iUserService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result dataList(@RequestBody User user, @RequestParam int currentPage, @RequestParam int pageSize) {
        Page<User> userPage = new Page<>(currentPage, pageSize);
        IPage<User> userIPage = userMapper.selectListByPage(userPage, user);
        return Result.ok().data("data", userIPage);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    public Result insertOrUpdate(@RequestBody User user) {
        Boolean b = iUserService.saveOrUpdate(user);

        return Result.ok().data("data", b);
    }

    @RequestMapping(value = "/insert")
    public Result dataInsert(@RequestBody User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", user.getUsername().toString());
        User selectUser = userMapper.selectOne(userQueryWrapper);
        System.out.println(selectUser);
        if (selectUser == null) {
            user.setPassword(MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
            System.out.println(user + "=========================");
            userMapper.insert(user);
            return Result.ok().data("data", user.getId());
        } else {
            return Result.error().message("用户名重复");
        }
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    //@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    //     public Result dataDelete(@PathVariable int id) {
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result dataDelete(@RequestBody User user) {
        // int deleteId = userMapper.deleteById(id);
        int deleteId = userMapper.deleteById(user.getId());
        return Result.ok().data("deleteId", deleteId);
    }

    @RequestMapping(value = "/batchdelete", method = RequestMethod.POST)
    public Result batchDelete(@RequestBody List<Integer> idList) {
        int deleteIds = userMapper.deleteBatchIds(idList);
        return Result.ok().data("data", deleteIds);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public Result dataModify(@RequestBody User user) {
        // userMapper.update()
        user.setPassword(MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
        int updateId = userMapper.updateById(user);
        return Result.ok().data("data", updateId);

    }
}
