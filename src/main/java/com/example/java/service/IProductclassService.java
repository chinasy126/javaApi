package com.example.java.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java.entity.Productclass;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author 名称
 * @since 2022-12-04
 */
public interface IProductclassService extends IService<Productclass> {
    IPage<Productclass> getDataByPage(int currentPage, int pageSize, Productclass productClass);

}
