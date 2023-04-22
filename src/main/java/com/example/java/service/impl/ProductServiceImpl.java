package com.example.java.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.Product;
import com.example.java.entity.Productclass;
import com.example.java.mapper.ProductMapper;
import com.example.java.service.IProductService;
import com.example.java.utils.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 名称
 * @since 2022-12-05
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    @Resource
    private ProductMapper productMapper;


    public IPage<Product> getDataByPage(int currentPage, int pageSize, Product product) {

        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
//        if (!StringUtils.isBlank(product.getName())) {
//            productQueryWrapper.like("name", product.getName());
//        }
        productQueryWrapper.orderByDesc("id");
        int current, size;
        size = pageSize;
        current = currentPage;
        Page<Product> page = new Page<>(current, size);
        IPage<Product> newsIPage = productMapper.selectListByPage(page, product.getName(), currentPage, pageSize);
        return newsIPage;
    }

}
