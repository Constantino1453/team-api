package com.itmk.web.goods_category.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.annotation.Auth;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.goods_category.entity.CategoryListParm;
import com.itmk.web.goods_category.entity.GoodsCategory;
import com.itmk.web.goods_category.entity.SelectType;
import com.itmk.web.goods_category.service.GoodsCategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@RequestMapping("/api/category")
@RestController
public class GoodsCategoryController {
    @Autowired
    private GoodsCategoryService goodsCategoryService;

    //新增
    @Auth
    @PostMapping
    public ResultVo add(@RequestBody GoodsCategory goodsCategory){
        if(goodsCategoryService.save(goodsCategory)){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //编辑
    @Auth
    @PutMapping
    public ResultVo edit(@RequestBody GoodsCategory goodsCategory){
        if(goodsCategoryService.updateById(goodsCategory)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @Auth
    @DeleteMapping("/{categoryId}")
    public ResultVo edit(@PathVariable("categoryId") Long categoryId){
        if(goodsCategoryService.removeById(categoryId)){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //列表
    @Auth
    @GetMapping("/list")
    public ResultVo list(CategoryListParm parm){
        //构造分页对象
        IPage<GoodsCategory> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        QueryWrapper<GoodsCategory> query = new QueryWrapper<>();
        query.lambda().like(StringUtils.isNotEmpty(parm.getCategoryName()),GoodsCategory::getCategoryName,parm.getCategoryName())
                .orderByAsc(GoodsCategory::getOrderNum);
        IPage<GoodsCategory> list = goodsCategoryService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    //小程序下拉数据
    @GetMapping("/getSelect")
    public ResultVo getSelect(){
        //查询所有的分类数据
        List<GoodsCategory> list = goodsCategoryService.list();
        //组装select需要的数据类型
        List<SelectType> selectTypeList = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item ->{
                    SelectType type = new SelectType();
                    type.setLabel(item.getCategoryName());
                    type.setValue(item.getCategoryId());
                    selectTypeList.add(type);
                });
        return ResultUtils.success("查询成功",selectTypeList);
    }

    //小程序分类接口
    @GetMapping("/getWxCateList")
    public ResultVo getWxCateList(){
        List<GoodsCategory> list = goodsCategoryService.list();
        return ResultUtils.success("查询成功",list);
    }
}
