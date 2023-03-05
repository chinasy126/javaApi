package com.example.java.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.Table;
import com.example.java.mapper.TableMapper;
import com.example.java.service.ITableService;
import com.example.java.vo.TableAndVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service

public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements ITableService {

    @Autowired
    @Resource
    private TableMapper tableMapper;

    @Override
    public IPage<Table> getAllData(int currentPage, int pageSize, Table table) {
        System.out.println(table);
        QueryWrapper<Table> tableQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(table.getTitle())) {
            tableQueryWrapper.like("title", table.getTitle());
        }

        if (!StringUtils.isBlank(table.getAuthor())) {
            tableQueryWrapper.like("author", table.getAuthor());
        }

        if (!StringUtils.isBlank(table.getPageviews())) {
            tableQueryWrapper.like("pageviews", table.getPageviews());
        }

        if (!StringUtils.isBlank(table.getStatus())) {
            tableQueryWrapper.eq("status", table.getStatus());
        }

        if (!StringUtils.isBlank(table.getDisplay_time())) {
            tableQueryWrapper.eq("display_time", table.getDisplay_time());
        }

        tableQueryWrapper.orderByDesc("id");
        int current, size;
        size = pageSize;
        current = currentPage;
        Page<Table> page = new Page<>(current, size);
        IPage<Table> tableIPage = tableMapper.selectPage(page, tableQueryWrapper);
        return tableIPage;
        //System.out.println(tableIPage);
    }

    /**
     * 查询分页
     *
     * @return
     */
    @Override
    public List<Table> selectTablePage(int currentPage, int pageSize) {
        QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", "123");
        /**
         *    加上false就不查询总记录数啦,如果不需要的话可以加上false，这样就会少一条sql语句
         *   Page<Student> page = new Page<>(current,size,false);
         */
        Page<Table> page = new Page<>(currentPage, pageSize, false);
        //   queryWrapper  可以为null  不进行条件判断
        //  返回list 调用  selectPage
        IPage<Table> tableIPage = tableMapper.selectPage(page, queryWrapper);
        List<Table> records = tableIPage.getRecords();
        return records;
    }


    /**
     * Map分页查询
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    public List<Map<String, Object>> selectTableMapPage(int currentPage, int pageSize) {
        // 1.条件查询
        QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("title", "123");
        // 2.设置分页
        Page<Map<String, Object>> tablePage = new Page<>(currentPage, pageSize);
        // 3 返回map调用  selectMapsPage
        IPage<Map<String, Object>> tableIPage = tableMapper.selectMapsPage(tablePage, queryWrapper);
        // 4 返回数据
        List<Map<String, Object>> records = tableIPage.getRecords();
        return records;
    }

//    @Override
//    public List<TableAndVo> selectTableAndXmlPage(int current, int size) {
//        QueryWrapper<TableAndVo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.gt("id", 19);
//
//        Page<TableAndVo> page = new Page<>(current, size, false);
//        IPage<TableAndVo> tableAndVoIPage = tableMapper.selectTableAndXmlPage(page, queryWrapper);
//        List<TableAndVo> records = tableAndVoIPage.getRecords();
//        return records;
//    }

}