package com.example.java.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java.entity.Productclass;
import com.example.java.mapper.ProductclassMapper;
import com.example.java.service.IProductclassService;
import com.example.java.vo.ProductClassVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author 名称
 * @since 2022-12-04
 */
@Service
public class ProductclassServiceImpl extends ServiceImpl<ProductclassMapper, Productclass> implements IProductclassService {

    @Autowired
    @Resource
    private ProductclassMapper productclassMapper;

    private String classPower = "0000";

    @Override
    public IPage<Productclass> getDataByPage(int currentPage, int pageSize, Productclass productClass) {
        QueryWrapper<Productclass> productClassQueryWrapper = new QueryWrapper<>();
        productClassQueryWrapper.orderByAsc("classpower");
        if (!StringUtils.isBlank(productClass.getClassname())) {
            productClassQueryWrapper.like("classname", productClass.getClassname());
        }
        productClassQueryWrapper.orderByDesc("classid");
        int current, size;
        size = pageSize;
        current = currentPage;
        Page<Productclass> page = new Page<>(current, size);
        IPage<Productclass> newsIPage = productclassMapper.selectPage(page, productClassQueryWrapper);

        return newsIPage;
    }

    /**
     * 转换分类信息
     * rootId
     * @param productClassVo
     */
    public Productclass returnClassifyInfo(Productclass productClassVo) {
        // 1 获取rootId . 获取rootId 如果为空则是一级分类，不为空则是多级分类，上一级别ID
        Integer rootId = productClassVo.getRootid() == null ? 0 : productClassVo.getRootid();

        // 2 获取 classpower
        Productclass productclass = new Productclass();
        // 2。2 数据库中不存在任何数据 则从0001 开始计数
        String classPower = this.classPower;
        if(rootId == 0){
            // 2。1 查询出 【一级分类里】 最大的分类转换成classPower
             productclass = productclassMapper.selectOne(new QueryWrapper<Productclass>()
                    .eq("depth", this.classPower.length())
                    .orderByDesc("classid").last("limit 1"));

            // 2。3 是属于一级分类获取到的classpower 【转换成真实的classpower】
            classPower = productclass == null ?
                    this.getClassPower(this.classPower) :
                    this.getClassPower(productclass.getClasspower());
        }else{
            // 首先查询上一级别所有信息 获取当前选择的信息
            productclass = productclassMapper.selectOne(new QueryWrapper<Productclass>().eq("classid", productClassVo.getRootid()));

            // 2。4 是属于多级分类获取到的classpower
            // 2。5 查询当前分类下所有子分类，子分类最大的classpower加1 当前选择下边所有的信息
           Productclass subClass = productclassMapper.selectOne(
                    new QueryWrapper<Productclass>()
                            .orderByDesc("classpower")
                            .eq("depth", productclass.getDepth() + this.classPower.length())
                            .likeRight("classpower", productclass.getClasspower())
                            .last("limit 1")
            );
           // 2.6 如果子分类不存在则直接 上一级分类的classpower+0001 , 如果存在就是最大的加1
            classPower = subClass == null ? this.getClassPower(productclass.getClasspower().concat(this.classPower)) :
                    this.getClassPower(subClass.getClasspower());
        }


        //  3 获取depth  获取深度，如果一级就是4 如果不是当前深度加4
        Integer depth = rootId == 0 ? this.classPower.length() : productclass.getDepth() + this.classPower.length();

        Productclass p = new Productclass();
        BeanUtils.copyProperties(productClassVo, p);
        p.setClasspower(classPower);
        p.setDepth(depth);
        p.setRootid(rootId);
        return p;
    }


    /**
     * 通过000加1
     *
     * @param pwCode
     * @return
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

}
