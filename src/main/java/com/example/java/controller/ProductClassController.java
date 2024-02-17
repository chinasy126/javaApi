package com.example.java.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.example.java.entity.News;
import com.example.java.entity.Productclass;
import com.example.java.mapper.ProductclassMapper;
import com.example.java.service.IProductclassService;
import com.example.java.utils.JwtUtils;
import com.example.java.utils.Result;
import com.example.java.vo.ProductClassVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/productclass")
public class ProductClassController {

    @Autowired
    private ProductclassMapper productclassMapper;

    @Autowired
    IProductclassService iProductclassService;

    private String classPower = "0000";


    /**
     * 数据列表
     *
     * @param productClass
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result dataList(@RequestBody Productclass productClass, @RequestParam int currentPage, @RequestParam int pageSize) {


        IPage<Productclass> productClassIPage = iProductclassService.getDataByPage(currentPage, pageSize, productClass);

        //   List<Integer> list = productClassIPage.getRecords().stream().map(Productclass::)

        //  IPage<ProductClassVo> iPage

        return Result.ok().data("data", productClassIPage);
    }

    /**
     * 插入数据
     *
     * @param productclass
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result dataInsert(@RequestBody ProductClassVo productClassVo) {

        Productclass productclass = new Productclass();
        // 拷贝内容
        BeanUtils.copyProperties(productClassVo, productclass);
        // 获取 classpower depth rootid
        Productclass p = iProductclassService.returnClassifyInfo(productclass);
        // 赋值 classpower depth rootid
        productclass.setClasspower(p.getClasspower());
        productclass.setRootid(p.getRootid());
        productclass.setDepth(p.getDepth());
        productclassMapper.insert(productclass);
        return Result.ok().data("insertId", productclass);
    }

    /**
     * 获取三件套
     *
     * @param classid classpower  depth rootid
     * @return
     */
    public Productclass convClassify(ProductClassVo productClassVo) {
        Productclass productclass = new Productclass();
        // 获取 classpower depth rootid
        Integer rootId = productClassVo.getRootid() == null ?
                0 : productClassVo.getRootid();
        // 获取上一级分类
        Productclass proData = productclassMapper.selectOne(new QueryWrapper<Productclass>().eq("classid", rootId));
        // 所选择分类
        String classPower;
        Integer deep;
        Integer root = rootId;
        if (proData == null) { // 为空则是一级分类
            // S 获取最大的一个分类
            Productclass productclassOne = productclassMapper.selectOne(new QueryWrapper<Productclass>()
                    .eq("depth", this.classPower.length())
                    .orderByDesc("classid").last("limit 1"));
            // E 获取最大的一个分类
            // 是否存在数据不存在新增一个
            classPower = productclassOne == null ?
                    this.getClassPower(this.classPower) :
                    this.getClassPower(productclassOne.getClasspower());
            // S 如果是修改的数据
            if (productClassVo.getClassid() != null) {
                // 修改范畴
                Productclass modifyData = productclassMapper.selectOne(
                        new QueryWrapper<Productclass>()
                                .eq("classid", productClassVo.getClassid()));
                if (modifyData.getDepth() == this.classPower.length()) {
                    classPower = modifyData.getClasspower();
                }
            }

            deep = this.classPower.length();
        } else {
            // 二级分类
            // 1 先查询父级数据内容
            Productclass productclassOne = productclassMapper.selectOne(
                    new QueryWrapper<Productclass>().eq("classid", productClassVo.getRootid())
            ); // 当前选中记录
            // 1 先查询父级下所有分类
            Productclass pClass = productclassMapper.selectOne(
                    new QueryWrapper<Productclass>()
                            .orderByDesc("classpower")
                            .eq("depth", productclassOne.getDepth() + this.classPower.length())
                            .likeRight("classpower", productclassOne.getClasspower())
                            .last("limit 1")
            );
            // 查询出来所有再选中分类下的内容
            classPower = pClass == null ?
                    this.getClassPower(productclassOne.getClasspower().concat(this.classPower)) :
                    this.getClassPower(pClass.getClasspower());
            deep = productclassOne.getDepth() + this.classPower.length();
            root = productclassOne.getClassid();
        }
        productclass.setClasspower(classPower);
        productclass.setDepth(deep);
        productclass.setRootid(root);
        return productclass;
    }

    /**
     * 获取ClassPower
     *
     * @param code
     * @return 1
     */
    public String getClassPower(String pwCode) {
        DecimalFormat decimalFormat = new DecimalFormat(this.classPower);
        if (pwCode.length() == this.classPower.length()) {
            int i = Integer.parseInt(pwCode) + 1;
            String k = decimalFormat.format(i);
            return k;
        } else {
            String code = pwCode;
            String codenew = code.substring((code.length() - this.classPower.length()), code.length());
            String frontCode = code.substring(0, (code.length() - this.classPower.length()));
            int i = Integer.parseInt(codenew) + 1;
            String k = decimalFormat.format(i);
            return frontCode + k;
        }
    }


    /**
     * 修改数据
     *
     * @param productclass
     * @param request
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Result dataModify(@RequestBody ProductClassVo productClassVo, HttpServletRequest request) {
        /**
         * 1、先查询出来当前数据
         * 2、修改当前数据
         * 如果 当前分类是否有改动
         * 	3、如果下边有分类则移动下边分类
         */
        Productclass productclass = new Productclass();
        BeanUtils.copyProperties(productClassVo, productclass);// 复制

        // 当前修改信息的数据库数据
        Productclass dataInfo = productclassMapper.selectOne(new QueryWrapper<Productclass>().eq("classid", productClassVo.getClassid()));
        // 如果相等则指修改数据信息不涉及分类信息
        if (dataInfo.getRootid().equals(productClassVo.getRootid()) || dataInfo.getRootid().equals(0) && productClassVo.getRootid() == null) {
            productclassMapper.update(productclass, new QueryWrapper<Productclass>().eq("classid", productclass.getClassid()));
        } else {
            // 查询 当前分类下是否还有其他分类
            List<Productclass> modifySubList = productclassMapper.selectList(
                    new QueryWrapper<Productclass>()
                            .orderByAsc("classpower")
                            .likeRight("classpower", dataInfo.getClasspower()));
            // 修改当前分类下存在其他分类则一起改变分类规则
            if (modifySubList.size() != 0) {
                Integer rootId = productClassVo.getRootid();
                for (Productclass pItem : modifySubList) {
                    pItem.setRootid(rootId);
                    //获取 classpower rootId depth
                    Productclass updateData = iProductclassService.returnClassifyInfo(pItem);
                    productclassMapper.update(updateData, new QueryWrapper<Productclass>().eq("classid", updateData.getClassid()));
                    rootId = updateData.getClassid();
                }
            }
        }
        return Result.ok().data("data", "ok");

    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result dataDelete(@RequestBody Productclass productclass) {
        int id = productclass.getClassid();
        QueryWrapper<Productclass> newsQueryWrapper = new QueryWrapper<Productclass>();
        newsQueryWrapper.eq("classid", id);
        int deleteId = productclassMapper.delete(newsQueryWrapper);
        return Result.ok().data("deleteId", deleteId);
    }

    /**
     * 获取所有分类
     *
     * @return
     */
    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public Result getClassList() {
        QueryWrapper<Productclass> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("classpower");
        List<Productclass> list = productclassMapper.selectList(queryWrapper);
        return Result.ok().data("data", list);
    }

}
