package com.example.java.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.java.entity.News;
import com.example.java.entity.User;
import com.example.java.mapper.NewsMapper;
import com.example.java.service.INewsService;
import com.example.java.utils.JwtUtils;
import com.example.java.utils.Result;
<<<<<<< HEAD
=======
import com.example.java.vo.ExportVo;
>>>>>>> master
import com.example.java.vo.NewsVo;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 名称
 * @since 2022-11-18
 */
@RestController
@CrossOrigin
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    INewsService iNewsService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 查询列表
     *
     * @param news
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result dataList(@RequestBody News news, @RequestParam int currentPage, @RequestParam int pageSize) {
        IPage<News> newsIPage = iNewsService.getDataByPage(currentPage, pageSize, news);
        System.out.println(newsIPage);
        return Result.ok().data("data", newsIPage);

    }

    /**
     * 插入新闻数据
     *
     * @param news
     * @param request
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result newsInsert(@RequestBody News news, HttpServletRequest request) {
        String xtoken = request.getHeader("X-Token");
        news.setAuthor(JwtUtils.getClaimsByToken(xtoken).getId());
        int insertId = newsMapper.insert(news);
        return Result.ok().data("insertId", insertId);
    }


    /**
     * 修改数据
     *
     * @param news
     * @param id
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Result newsModify(@RequestBody News news, HttpServletRequest request) {
        QueryWrapper<News> newsQueryWrapper = new QueryWrapper<News>();

        if (news.getId() != null) {
            newsQueryWrapper.eq("id", news.getId());
            int resultId = newsMapper.update(news, newsQueryWrapper);
            return Result.ok().data("data", resultId);
        } else {
            String xtoken = request.getHeader("X-Token");
            news.setAuthor(JwtUtils.getClaimsByToken(xtoken).getId());
            int insertId = newsMapper.insert(news);
            return Result.ok().data("insertId", insertId);
        }
    }

    /**
     * 新增或者修改
     *
     * @param news
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result insertOrUpdate(@RequestBody News news, HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        news.setAuthor(JwtUtils.getClaimsByToken(token).getId());
        Boolean b = iNewsService.saveOrUpdate(news);
        return Result.ok().data("ok", b);
    }

    /**
     * 批量删除
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.DELETE)
    public Result batchDelete(@RequestBody Map<String, List<Integer>> map) {
        List<Integer> ids = map.get("id");
        if (ids.size() > 0) {
            int deleteId = newsMapper.deleteBatchIds(ids);
            return Result.ok().data("ok", deleteId);
        } else {
            return Result.error().message("请选择要删除得数据");
        }


    }


    /**
     * 删除数据
     *
     * @param id
     * @return
     */
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
//    public Result newsDelete(@PathVariable int id) {
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result newsDelete(@RequestBody News news) {
        int id = news.getId();
        QueryWrapper<News> newsQueryWrapper = new QueryWrapper<News>();
        newsQueryWrapper.eq("id", id);
        int deleteId = newsMapper.delete(newsQueryWrapper);
        return Result.ok().data("deleteId", deleteId);
    }

    /**
     * 查询单挑记录，详情或编辑
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public Result newsDetail(@RequestBody Map<String, Integer> map) {
        int id = map.get("id");
        News newsDetail = new News();
        newsDetail = newsMapper.selectById(id);
        return Result.ok().data("data", newsDetail);
    }

    /**
     * 修改数据
     *
     * @param news
     * @param id
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result newsUpdate(@RequestBody News news, @RequestParam int id) {
        QueryWrapper<News> newsQueryWrapper = new QueryWrapper<News>();
        newsQueryWrapper.eq("id", id);
        int resultId = newsMapper.update(news, newsQueryWrapper);
        return Result.ok().data("data", resultId);
    }


    /**
     * 下载Excel
     *
     * @param response
     */
    @PostMapping("/export")
    @ApiOperation(value = "新闻信息导出", notes = "/export")
<<<<<<< HEAD
    public void export(@RequestBody News news, HttpServletResponse response) {
=======
//    public void export(@RequestBody ExportVo news, HttpServletResponse response) {
    public void export(@RequestBody Map<String,Object> news, HttpServletResponse response) {

        List<Integer> ids = (List<Integer>) news.get("id");
>>>>>>> master

        try {
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=user.xls");
            ExportParams exportParams = new ExportParams();

            QueryWrapper<News> queryWrapper = new QueryWrapper<>();
<<<<<<< HEAD
            if (StringUtils.isNotBlank(news.getTitle())) {
//                queryWrapper.eq("title",news.getTitle());
                queryWrapper.like("title", news.getTitle());
            }

            if (news.getUpdate() != null) {
                //if (!StringUtils.isBlank(news.getUpdate())) {
                queryWrapper.like("`update`", news.getUpdate());
            }
=======
//            if (StringUtils.isNotBlank(news.getTitle())) {
////                queryWrapper.eq("title",news.getTitle());
//                queryWrapper.like("title", news.getTitle());
//            }
//
//            if (news.getUpdate() != null) {
//                //if (!StringUtils.isBlank(news.getUpdate())) {
//                queryWrapper.like("`update`", news.getUpdate());
//            }

            if (ids.size() > 0) {
                queryWrapper.in("id", ids);
            }

>>>>>>> master
            List<News> newsList = newsMapper.selectList(queryWrapper);
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, News.class, newsList);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            logger.error("This is an error message");

        }
    }

    /**
     * 上传Excel
     *
     * @param file
     * @return
     */
    @PostMapping("/import")
    public Result importExcel(@RequestParam("file") MultipartFile file) {
        ImportParams importParams = new ImportParams();
        importParams.setNeedVerfiy(false);
        try {
            ExcelImportResult<News> result =
                    ExcelImportUtil.importExcelMore(file.getInputStream(), News.class, importParams);
            // 当这里就把excel文件的内容根据@Excel注解转换成了对应的实体对象，后续的入库操作这里就省略了
            List<News> newsList = result.getList();
            iNewsService.saveBatch(newsList);
            // 入库操作 。。。。
            logger.info("从Excel导入数据一共 {} 行 ", newsList.size());
        } catch (IOException e) {
            logger.error("导入失败：{}", e.getMessage());
        } catch (Exception e1) {
            logger.error("导入失败：{}", e1.getMessage());
        }
<<<<<<< HEAD
        return Result.ok().data("ok","导入成功");
=======
        return Result.ok().data("ok", "导入成功");
>>>>>>> master
    }


}
