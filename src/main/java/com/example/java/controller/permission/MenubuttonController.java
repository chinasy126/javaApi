package com.example.java.controller.permission;

import com.example.java.service.permission.IMenubuttonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 名称
 * @since 2022-12-23
 */
@RestController
@CrossOrigin
@RequestMapping("/menuButton/menubutton")
public class MenubuttonController {

    @Autowired
    IMenubuttonService iMenubuttonService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public void list() {

        System.out.println("===========================");
    }

}
