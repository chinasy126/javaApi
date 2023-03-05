package com.example.java.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java.entity.Table;
import com.example.java.vo.TableAndVo;

import java.util.List;
import java.util.Map;

public interface ITableService extends IService<Table> {

    /**
     * 复杂分页查询
     *
     * @param currentPage
     * @param pageSize
     * @param table
     * @return
     */
    IPage<Table> getAllData(int currentPage, int pageSize, Table table);

    /**
     * 简单分页查询
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<Table> selectTablePage(int currentPage, int pageSize);

    /**
     * Map 简单分页
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> selectTableMapPage(int currentPage, int pageSize);

//    List<TableAndVo> selectTableAndXmlPage(int current, int size);

}
