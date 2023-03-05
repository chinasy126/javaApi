package com.example.java.vo.menus;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Stack;

@Data
public class BatchMenuVo {

    private String name;

    private String title;

    // List<String> button;
    // List<Map<String, String>> button;
    // List<Map<Object, Object>> button;
    List<Object> button;

    private Map meta;

    List<BatchMenuVo> children;


}
