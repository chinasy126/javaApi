package com.example.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java.entity.Table;
import com.example.java.vo.TableAndVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.core.conditions.Wrapper;


@Mapper
@Component
public interface TableMapper extends BaseMapper<Table> {


   // IPage<TableAndVo> selectTableAndXmlPage(Page<TableAndVo> page, @Param(Constants.WRAPPER) Wrapper<TableAndVo> wrapper );

}
