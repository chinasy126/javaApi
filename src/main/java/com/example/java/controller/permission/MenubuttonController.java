package com.example.java.controller.permission;

import com.example.java.entity.permission.Menubutton;
import com.example.java.mapper.permission.MenubuttonMapper;
import com.example.java.service.permission.IMenubuttonService;
import com.example.java.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 名称
 * @since 2022-12-23
 */
@RestController
@CrossOrigin
@RequestMapping("/menubutton")
public class MenubuttonController {

    @Autowired
    IMenubuttonService iMenubuttonService;

    @Autowired
    private MenubuttonMapper menubuttonMapper;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public void list() {

        System.out.println("===========================");
    }

    /**
     * 删除按钮
     *
     * @param menubutton
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result deleteBtn(@RequestBody Menubutton menubutton) {
        Boolean b = iMenubuttonService.removeById(menubutton.getId());
        if (b)
            return Result.ok().data("status", b);
        return Result.error();
    }

    /**
     * 插入按钮数据
     * @param menubutton
     * @return
     */
    @RequestMapping(value = "/indertBtn", method = RequestMethod.POST)
    public Result insertBtn(@RequestBody Menubutton menubutton) {
        int insertId = menubuttonMapper.insert(menubutton);
        return Result.ok().data("data", insertId);
    }

}
