package com.example.java.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class NewsVo {

    @Excel(name = "ID", width = 10)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    @Excel(name="标题")
    private String title;

    /**
     * 副标题
     */
   // @Excel(name="副标题")
    private String fTitle;

    /**
     * 图片
     */
    private String pic;

    /**
     * 缩略图
     */
    private String s_pic;

    /**
     * 内容
     */
    private String contents;

    /**
     * 时间
     */
 //   @Excel(name = "时间", width = 20, format = "yyyy-MM-dd")
    @TableField(value = "`update`")
    private String update;

    /**
     * 点击数
     */
    private Integer num;

    /**
     * 推荐值
     */
    private Integer top;

    /**
     * 作者
     */
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
