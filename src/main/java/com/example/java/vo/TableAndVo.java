package com.example.java.vo;


import io.swagger.models.auth.In;

import java.io.Serializable;


public class TableAndVo implements Serializable {
    private Integer id;
    private String title;
    private String author;

    @Override
    public String toString() {
        return "TableAndVo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

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
}
