package com.example.java.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.Productclass;
import com.example.java.mapper.ProductclassMapper;
import com.example.java.service.IProductclassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author 名称
 * @since 2022-12-04
 */
@Service
public class ProductclassServiceImpl extends ServiceImpl<ProductclassMapper, Productclass> implements IProductclassService {

    @Autowired
    @Resource
    private ProductclassMapper productclassMapper;

    @Override
    public IPage<Productclass> getDataByPage(int currentPage, int pageSize, Productclass productClass) {
        QueryWrapper<Productclass> productClassQueryWrapper = new QueryWrapper<>();
        productClassQueryWrapper.orderByAsc("classpower");
        if (!StringUtils.isBlank(productClass.getClassname())) {
            productClassQueryWrapper.like("classname", productClass.getClassname());
        }
        productClassQueryWrapper.orderByDesc("classid");
        int current, size;
        size = pageSize;
        current = currentPage;
        Page<Productclass> page = new Page<>(current, size);
        IPage<Productclass> newsIPage = productclassMapper.selectPage(page, productClassQueryWrapper);

        return newsIPage;
    }

}
