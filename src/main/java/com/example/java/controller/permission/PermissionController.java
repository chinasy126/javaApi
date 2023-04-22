package com.example.java.controller.permission;

import com.example.java.utils.JwtUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/permission")
public class PermissionController {

//    @RequestMapping(value = "menulist", method = RequestMethod.POST)
//    public void getMenuList(HttpServletRequest request){
////        String xtoken = request.getHeader("X-Token");
////        news.setAuthor(JwtUtils.getClaimsByToken(xtoken).getId());
//    }

}
