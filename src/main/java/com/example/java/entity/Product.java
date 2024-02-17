package com.example.java.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 名称
 * @since 2022-12-05
 */
@Data
public class Product implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer pid;

    @TableField(exist = false)
    private String classname;

    @TableField(exist = false)
    Productclass productclass;

    private String name;

    private String title;

    private String pic;

    private String contents;

    private Integer num;

    private Integer top;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "`update`")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate update;

    private String author;

    /**
     * 网页标题
     */
    private String webtitle;

    /**
     * 网页关键词
     */
    private String webkey;

    /**
     * 网页描述
     */
    private String webdes;
}
