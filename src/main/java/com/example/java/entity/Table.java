package com.example.java.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author 名称
 * @since 2022-10-05
 */
@TableName(value = "`table`")
public class Table{


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "title")
    private String title;
    @TableField(value = "author")
    private String author;
    @TableField(value = "pageviews")
    private String pageviews;

    @TableField(value = "`status`")
    private String status;
    @TableField(value = "display_time")
    private String display_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getPageviews() {
        return pageviews;
    }

    public void setPageviews(String pageviews) {
        this.pageviews = pageviews;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplay_time() {
        return display_time;
    }

    public void setDisplay_time(String display_time) {
        this.display_time = display_time;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pageviews='" + pageviews + '\'' +
                ", status='" + status + '\'' +
                ", display_time='" + display_time + '\'' +
                '}';
    }
}
