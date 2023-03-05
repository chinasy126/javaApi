package com.example.java.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.java.entity.News;
import com.example.java.entity.Product;
import com.example.java.entity.Productclass;
import com.example.java.mapper.ProductMapper;
import com.example.java.service.IProductService;
import com.example.java.utils.JwtUtils;
import com.example.java.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 名称
 * @since 2022-12-05
 */
@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProductController extends CommonController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    IProductService iProductService;

    /**
     * 数据查询
     *
     * @param product
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result dataList(@RequestBody Product product, @RequestParam int currentPage, @RequestParam int pageSize) {
        return Result.ok().data("data", iProductService.getDataByPage(currentPage, pageSize, product));
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result dataDelete(@RequestBody Product product) {
        int id = product.getId();
        QueryWrapper<Product> newsQueryWrapper = new QueryWrapper<>();
        newsQueryWrapper.eq("id", id);
        int deleteId = productMapper.delete(newsQueryWrapper);
        return Result.ok().data("deleteId", deleteId);
    }


    /**
     * 插入数据
     *
     * @param productclass
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result dataInsert(@RequestBody Product product) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper();
        int insertId = productMapper.insert(product);
        return Result.ok().data("insertId", insertId);
    }

    /**
     * 产品新增或者修改
     * @param product
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result insertOrUpdate(@RequestBody Product product, HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        product.setAuthor(JwtUtils.getClaimsByToken(token).getId());
        Boolean b = iProductService.saveOrUpdate(product);
        return Result.ok().data("ok", b);
    }


}
