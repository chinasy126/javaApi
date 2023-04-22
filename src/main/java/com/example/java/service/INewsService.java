package com.example.java.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java.entity.News;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 名称
 * @since 2022-11-18
 */
public interface INewsService extends IService<News> {

    /**
     * 获取分页数据
     * @param currentPage
     * @param pageSize
     * @param news
     * @return
     */
    IPage<News> getDataByPage(int currentPage, int pageSize, News news);


}

