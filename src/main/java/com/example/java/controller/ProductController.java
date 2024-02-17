package com.example.java.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.java.entity.News;
import com.example.java.entity.Product;
import com.example.java.entity.Productclass;
import com.example.java.mapper.ProductMapper;
import com.example.java.service.IProductService;
import com.example.java.utils.JwtUtils;
import com.example.java.utils.QiniuImageUtils;
import com.example.java.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 名称
 * @since 2022-12-05
 */
@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProductController extends CommonController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    IProductService iProductService;

    @Autowired
    QiniuImageUtils qiniuImageUtils;

    @Value("${qiniu.domain}")
    private String imageDomain;

    /**
     * 数据查询
     *
     * @param product
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result dataList(@RequestBody Product product, @RequestParam int currentPage, @RequestParam int pageSize) {
        return Result.ok().data("data", iProductService.getDataByPage(currentPage, pageSize, product));
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result dataDelete(@RequestBody Product product) {
        int id = product.getId();
        QueryWrapper<Product> newsQueryWrapper = new QueryWrapper<>();
        newsQueryWrapper.eq("id", id);
        int deleteId = productMapper.delete(newsQueryWrapper);
        return Result.ok().data("deleteId", deleteId);
    }


    /**
     * 插入数据
     *
     * @param productclass
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result dataInsert(@RequestBody Product product) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper();
        int insertId = productMapper.insert(product);
        return Result.ok().data("insertId", insertId);
    }

    /**
     * 产品新增或者修改
     *
     * @param product
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result insertOrUpdate(@RequestBody Product product, HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        product.setAuthor(JwtUtils.getClaimsByToken(token).getId());

        String res = converToRelativePath(product.getContents());
        product.setContents(res);
        Boolean b = iProductService.saveOrUpdate(product);
        return Result.ok().data("ok", b);
    }

    /**
     * 查询产品详情
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Result detail(@RequestBody Product product) {
        Product product1 = productMapper.selectById(product.getId());

        String contents = convertRelativePathToFullUrl(product1.getContents());
        product1.setContents(contents);
        return Result.ok().data("data", product1);
    }


    private String converToRelativePath(String content) {
        // 匹配img标签中的src属性的正则表达式
        String regex = "<img\\s+src=\"(.*?)\"(.*?)>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        StringBuffer result = new StringBuffer();

        // 遍历匹配结果
        while (matcher.find()) {
            String originalSrc = matcher.group(1);
            String newSrc = extractFileName(originalSrc);
            // 将匹配到的src属性值替换成新的值，并保留其他部分
            matcher.appendReplacement(result, "<img src=\"" + newSrc + "\"" + matcher.group(2) + ">");
        }

        // 将剩余的部分添加到结果中
        matcher.appendTail(result);

        return result.toString();
    }

    private static String extractFileName(String imageURL) {
        try {
            // 使用URL类解析URL
            URL url = new URL(imageURL);
            // 获取URL路径中的文件名部分
            String fileName = url.getPath();
            if (fileName.startsWith("/")) {
                return fileName.substring(1);
            }
            return fileName;
        } catch (MalformedURLException e) {
            // 处理URL格式错误的情况
            e.printStackTrace();
            return imageURL;
        }
    }

    /**
     * 转换图片的地址
     *
     * @param content
     * @return
     */
    public String convertRelativePathToFullUrl(String content) {
        // 使用正则表达式匹配<img>标签中的相对路径
        String pattern = "<img\\s+src=\"(.*?)\"";
        Pattern imgPattern = Pattern.compile(pattern);
        Matcher matcher = imgPattern.matcher(content);
        // 替换相对路径为完整的图片地址
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String relativePath = matcher.group(1);
            String fullUrl = qiniuImageUtils.generatePrivateImageUrl(relativePath);
            matcher.appendReplacement(result, Matcher.quoteReplacement("<img src=\"" + fullUrl + "\""));
        }
        matcher.appendTail(result);
        return result.toString();
    }

}
