package com.example.java.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java.entity.Product;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 名称
 * @since 2022-12-05
 */
public interface IProductService extends IService<Product> {

    IPage<Product> getDataByPage(int currentPage, int pageSize, Product product);
}
