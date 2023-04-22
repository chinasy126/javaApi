package com.example.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.java.entity.News;
import com.example.java.mapper.NewsMapper;
import com.example.java.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 名称
 * @since 2022-11-18
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

    @Autowired
    @Resource
    private NewsMapper newsMapper;

    @Override
    public IPage<News> getDataByPage(int currentPage, int pageSize, News news) {

        QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(news.getTitle())) {
            newsQueryWrapper.like("title", news.getTitle());
        }

//        news.getUpdate()
        LocalDate date = news.getUpdate();
//         Date date = news.getUpdate();
          if(date != null){
       // if (!StringUtils.isBlank(news.getUpdate())) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String dateTimeStr2 = sdf.format(news.getUpdate());
//            newsQueryWrapper.eq("`update`", dateTimeStr2);
            newsQueryWrapper.eq("`update`", news.getUpdate());
        }
//
        newsQueryWrapper.orderByDesc("id");
        int current, size;
        size = pageSize;
        current = currentPage;
        Page<News> page = new Page<>(current, size);
        IPage<News> newsIPage = newsMapper.selectPage(page, newsQueryWrapper);
        return newsIPage;

    }

}
