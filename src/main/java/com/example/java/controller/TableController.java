package com.example.java.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java.entity.Table;
import com.example.java.entity.User;
import com.example.java.mapper.TableMapper;
import com.example.java.mapper.UserMapper;
import com.example.java.service.ITableService;

import com.example.java.utils.Result;
import com.example.java.vo.TableAndVo;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@CrossOrigin
@RequestMapping("/table")

public class TableController {

    @Autowired
    private TableMapper tableMapper;

    @Autowired
    ITableService iTableService;

    /**
     * 复杂分页查询
     *
     * @param table
     * @param currentPage
     * @param pageSize
     * @return
     */

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result dataList(@RequestBody Table table, @RequestParam int currentPage, @RequestParam int pageSize) {
        IPage<Table> tableIPage = iTableService.getAllData(currentPage, pageSize, table);
        return Result.ok().data("data", tableIPage);
    }

    /**
     * Map 简单分页
     *
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/selectTableMapPage/{current}/{size}")
    public Object selectTableMapPage(@PathVariable int current, @PathVariable int size) {
        List<Map<String, Object>> maps = iTableService.selectTableMapPage(current, size);

        List<String> wordList = Arrays.asList("Java 8", "Lambdas", "In", "Action");

        List<String> strList = wordList.stream()
                .map(w -> w.split(" "))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
System.out.println(strList);
        return maps;
    }

    /**
     * 简单分页查询
     *
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/selectTablePage/{current}/{size}")
    public Object selectTablePage(@PathVariable int current, @PathVariable int size) {
        List<Table> tableList = iTableService.selectTablePage(current, size);
        return tableList;
    }


//    @GetMapping("/selectTableAndXmlPage/{current}/{size}")
//    public Object selectTableAndXmlPage(@PathVariable int current, @PathVariable int size) {
//        List<TableAndVo> tableAndVoList = iTableService.selectTableAndXmlPage(current,size);
//        return tableAndVoList;
//    }

    /**
     * 通过ID POST 获取单条数据
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public Result find(@RequestBody Map<String, Integer> map) {
        int id = map.get("id");
        System.out.println(tableMapper.selectById(id));
        Table table = new Table();
        table = tableMapper.selectById(id);
        return Result.ok().data("data", table);
        //  System.out.println(table.getId());
    }

    /**
     * 修改数据 | 更新数据
     *
     * @param table
     * @param id
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody Table table, @RequestParam int id) {
        QueryWrapper<Table> queryWrapper = new QueryWrapper<Table>();
        queryWrapper.eq("id", id);
        int resultId = tableMapper.update(table, queryWrapper);
        System.out.println(table);
        System.out.println(id);
        return Result.ok().data("data", resultId);
    }


    /**
     * 插入数据
     *
     * @param table
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody Table table) {
        int insertId = tableMapper.insert(table);
        System.out.println(table);
        return Result.ok().data("insertId", insertId);
        //return insertId;
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable int id) {
        QueryWrapper<Table> tableQueryWrapper = new QueryWrapper<Table>();
        tableQueryWrapper.eq("id", id);
        int deleteId = tableMapper.delete(tableQueryWrapper);
        System.out.println(id);
        return Result.ok().data("deleteId", deleteId);
    }


    /**
     * 批量ID查询
     *
     * @return
     */
    @GetMapping("/show")
    public List selectByIds() {
        List<Long> ids = Arrays.asList(1L, 124L, 3L);
        List<Table> tables = tableMapper.selectBatchIds(ids);
        System.out.println("=======" + tables + "==========");
        return tables;
    }


    /**
     * 条件查询
     *
     * @param title
     * @param author
     * @return
     */

    @RequestMapping(value = "/selectbywrapper", method = RequestMethod.GET)
    public List selectByWrapper(@RequestParam String title, String author) {
        QueryWrapper<Table> tableQueryWrapper = new QueryWrapper<Table>();

        tableQueryWrapper.like("title", title)
                .orderByDesc("id")
                .like(StringUtils.hasText(author), "author", author);
        List<Table> tableList = tableMapper.selectList(tableQueryWrapper);
        tableList.forEach(System.out::println);
        return tableList;
    }

    public void showData() {
        selectByIds();
    }

    @RequestMapping(value = "/list1", method = RequestMethod.POST)
    // @GetMapping("/list")
    public Result getTableList() {
//        Page<User> page = new Page<>(0,2);
//        IPage iPage = userMapper.selectPage(page,null);
//        return iPage;

        QueryWrapper<Table> queryWrapper = new QueryWrapper<Table>();
        queryWrapper.orderByDesc("id");
        List<Table> tableList = tableMapper.selectList(queryWrapper);
//        Page<Table> page = new Page<>(0, 100);
//        IPage iPage = tableMapper.selectPage(page, null);
//        System.out.println(iPage);
        return Result.ok().data("data", tableList);


        /// return iPage;
//        return iPage;

//        int id = 1;
//        // User user = userMapper.selectById(id);
//        Table table = tableMapper.selectById(id);
//        System.out.println(table);
//        System.out.println(id+"=============");

    }

}
