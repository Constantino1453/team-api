package com.itmk.web.sys_banner.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.annotation.Auth;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.service.GoodsService;
import com.itmk.web.goods_category.entity.SelectType;
import com.itmk.web.sys_banner.entity.BannerPage;
import com.itmk.web.sys_banner.entity.SysBanner;
import com.itmk.web.sys_banner.service.SysBannerService;
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
@RequestMapping("/api/banner")
@RestController
public class SysBannerController {
    @Autowired
    private SysBannerService sysBannerService;
    @Autowired
    private GoodsService goodsService;

    //新增
    @PostMapping
    @Auth
    public ResultVo add(@RequestBody SysBanner sysBanner){
        if(sysBannerService.save(sysBanner)){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }
     //新增
    @PostMapping("/upandown")
    @Auth
    public ResultVo upandown(@RequestBody SysBanner sysBanner){
        UpdateWrapper<SysBanner> query = new UpdateWrapper<>();
        query.lambda().set(SysBanner::getStatus,sysBanner.getStatus());
        if(sysBannerService.update(query)){
            return ResultUtils.success("操作成功!");
        }
        return ResultUtils.error("操作失败!");
    }

    //编辑
    @PutMapping
    @Auth
    public ResultVo edit(@RequestBody SysBanner sysBanner){
        if(sysBannerService.updateById(sysBanner)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @Auth
    @DeleteMapping("/{banId}")
    public ResultVo delete(@PathVariable("banId") Long banId){
        if(sysBannerService.removeById(banId)){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //列表
    @Auth
    @GetMapping("/list")
    public ResultVo list(BannerPage parm){
        //构造分页对象
        IPage<SysBanner> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        QueryWrapper<SysBanner> query = new QueryWrapper<>();
        query.lambda().like(StringUtils.isNotEmpty(parm.getTitle()),SysBanner::getTitle,parm.getTitle());
        //获取列表
        IPage<SysBanner> list = sysBannerService.page(page, query);
        return ResultUtils.success("查询成",list);
    }

    //物品下来数据
    @GetMapping("/selectGoods")
    public ResultVo getGoodsSelect(){
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.lambda().eq(Goods::getStatus,"0")
                .eq(Goods::getDeleteStatus,"0")
                .eq(Goods::getSellStatus,"0");
        List<Goods> list = goodsService.list(query);
        //组装下拉数据
        List<SelectType> selectTypeList = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .forEach(item -> {
//                    selectTypeList.add(new SelectType(item.getGoodsId(),item.getTitle()));
                    SelectType type = new SelectType();
                    type.setLabel(item.getTitle());
                    type.setValue(item.getGoodsId());
                    selectTypeList.add(type);
                });
        return ResultUtils.success("查询成功",selectTypeList);
    }

    //小程序首页轮播图
    @GetMapping("/getIndexBanner")
    public ResultVo getIndexBanner(){
        List<SysBanner> list = sysBannerService.list();
        //设置对应的物品
        if(list.size() > 0){
            for (int i = 0;i<list.size();i++){
                //查询对应的物品
                Goods goods = goodsService.getById(list.get(i).getGoodsId());
                //设置轮播图对应的物品
                list.get(i).setGoods(goods);
            }
        }
        return ResultUtils.success("查询成功",list);
    }
}
