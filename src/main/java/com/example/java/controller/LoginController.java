package com.example.java.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.java.entity.News;
import com.example.java.entity.User;
import com.example.java.entity.permission.*;
import com.example.java.mapper.UserMapper;
import com.example.java.mapper.permission.MenuMapper;
import com.example.java.mapper.permission.RoleButtonsMapper;
import com.example.java.mapper.permission.RolemenusMapper;
import com.example.java.service.permission.IMenuService;
import com.example.java.utils.JwtUtils;
import com.example.java.utils.MD5Util;
import com.example.java.utils.RandomValidateCodeUtil;
import com.example.java.utils.Result;
import com.example.java.vo.menus.MenusVo;
import com.example.java.vo.menus.UserVo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin

@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RolemenusMapper rolemenusMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleButtonsMapper roleButtonsMapper;

    @Autowired
    IMenuService iMenuService;

    @ApiModelProperty(value = "用户相关信息")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody UserVo user, HttpSession httpSession) {

        String random = (String) httpSession.getAttribute("RANDOMVALIDATECODEKEY");// 从缓存中获取验证码
        if (!StringUtils.hasText(user.getCode()) || !user.getCode().equals(random)) {
            return Result.error().message("验证码输入错误");
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        User user1 = new User();
        userQueryWrapper.eq("username", user.getUsername()).eq("password", MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
        user1 = userMapper.selectOne(userQueryWrapper);
        if (user1 == null || "".equals(user1)) {
            return Result.error().message("用户名或密码错误");
        } else {
            String token = JwtUtils.generateToken(user.getUsername(), Integer.toString(user1.getId()), user1.getAvatar());
            return Result.ok().data("token", token);
        }
    }

    /**
     * 查询用户 信息
     *
     * @param token
     * @return
     */
    @ApiModelProperty(value = "登录后获取用户信息")
    @GetMapping("/info")
    public Result info(String token) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        Map<String, Object> map = (Map) JwtUtils.getClaimsByToken(token);
        return Result.ok().data("name", username).data("avatar", map.get("avatar"));
    }

    /**
     * 用户信息以及菜单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/nav", method = RequestMethod.POST)
    public Result nav(HttpServletRequest request) {
        // 登录之后获取用户的ID
        String xtoken = request.getHeader("X-Token");
        String userId = JwtUtils.getClaimsByToken(xtoken).getId();
        // 获取出来用户所有信息
        User userInfo = userMapper.selectById(Integer.parseInt(userId));

        userInfo.setMenusList(iMenuService.getRoleMenuButtons(userInfo));
        return Result.ok().data("data", userInfo);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        // 删除包含 JWT 的 Cookie
        Cookie cookie = new Cookie("jwt", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 删除 Authorization 头
        response.setHeader("Authorization", "");

        // 返回注销成功的响应
        response.setStatus(HttpStatus.OK.value());
        return Result.ok().data("msg","ok").message("退出成功!");
    }


    @ApiOperation(value = "生成验证码")
    @RequestMapping(value = "/verificationcode", method = RequestMethod.POST)
    public void verifCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            System.out.println("获取验证码失败>>>>   ");
        }
    }


}
