package com.example.java.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 名称
 * @since 2022-11-18
 */
@Data
public class News implements Serializable {
    //  @Excel(name = "ID", width = 10)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    @Excel(name = "标题", width = 10)
    private String title;

    /**
     * 副标题
     */
    @Excel(name = "副标题")
    @TableField(value = "`fTitle`")
    private String fTitle;

    /**
     * 图片
     */

//    @Excel(name = "图片",type = 2 ,width = 40 , height = 20,imageType = 1)
    @Excel(name = "图片")
    private String pic;

    /**
     * 缩略图
     */
    private String s_pic;

    /**
     * 内容
     */
    @Excel(name = "内容")
    private String contents;

    /**
     * 时间
     */
//    @Excel(name="时间",format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "`update`")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate update;
//    private Date update;
//    @Excel(name="时间")
//    @TableField(value = "`update`")
//    private String update;
    /**
     * 点击数
     */
    @Excel(name = "点击数")
    private Integer num;

    /**
     * 推荐值
     */
    @Excel(name = "推荐值")
    private Integer top;

    /**
     * 作者
     */
    @Excel(name = "作者")
    private String author;

    /**
     * 网页标题
     */
    @Excel(name = "网页标题")
    private String webtitle;

    /**
     * 网页关键词
     */
    @Excel(name = "网页关键词")
    private String webkey;

    /**
     * 网页描述
     */
    @Excel(name = "网页描述")
    private String webdes;







}

//name：Excel中的列名；
//width：指定列的宽度；
//needMerge：是否需要纵向合并单元格；
//format：当属性为时间类型时，设置时间的导出导出格式；
//desensitizationRule：数据脱敏处理，3_4表示只显示字符串的前3位和后4位，其他为*号；
//replace：对属性进行替换；
//suffix：对数据添加后缀。
//