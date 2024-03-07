package com.itmk.web.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.annotation.Auth;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.entity.GoodsPage;
import com.itmk.web.goods.entity.WxGoodsList;
import com.itmk.web.goods.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@RequestMapping("/api/goods")
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    //发布
    @PostMapping("/release")
    public ResultVo add(@RequestBody Goods goods){
        goods.setCreateTime(new Date());
        if(goodsService.save(goods)){
            return ResultUtils.success("发布成功!");
        }
        return ResultUtils.error("发布失败!");
    }

    //列表
    @Auth
    @GetMapping("/list")
    public ResultVo getList(GoodsPage parm){
        //构造分页对象
        IPage<Goods> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.lambda().eq(StringUtils.isNotEmpty(parm.getType()),Goods::getType,parm.getType())
                .like(StringUtils.isNotEmpty(parm.getTitle()),Goods::getTitle,parm.getType())
                .like(StringUtils.isNotEmpty(parm.getUserName()),Goods::getUserName,parm.getUserName())
                .eq(Goods::getDeleteStatus,"0")
                .orderByDesc(Goods::getCreateTime);
        IPage<Goods> list = goodsService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    //删除
    @Auth
    @PostMapping("/delete")
    public ResultVo delete(@RequestBody Goods goods){
        UpdateWrapper<Goods> query = new UpdateWrapper<>();
        query.lambda().set(Goods::getDeleteStatus,"1")
                .eq(Goods::getGoodsId,goods.getGoodsId());
        if(goodsService.update(query)){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //上下架
    @Auth
    @PostMapping("/upandown")
    public ResultVo upandown(@RequestBody Goods goods){
        UpdateWrapper<Goods> query = new UpdateWrapper<>();
        query.lambda().set(Goods::getStatus,goods.getStatus())
                .eq(Goods::getGoodsId,goods.getGoodsId());
        if(goodsService.update(query)){
            return ResultUtils.success("操作成功!");
        }
        return ResultUtils.error("操作失败!");
    }

    //设置热推
    @Auth
    @PostMapping("/setIndex")
    public ResultVo setIndex(@RequestBody Goods goods){
        UpdateWrapper<Goods> query = new UpdateWrapper<>();
        query.lambda().set(Goods::getSetIndex,goods.getSetIndex())
                .eq(Goods::getGoodsId,goods.getGoodsId());
        if(goodsService.update(query)){
            return ResultUtils.success("设置成功!");
        }
        return ResultUtils.error("设置失败!");
    }

    //小程序热推数据查询
    @GetMapping("/getIndexList")
    public ResultVo getIndexList(WxGoodsList parm){
        //构造分页对象
        IPage<Goods> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //条件查询
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.lambda().eq(Goods::getSetIndex,"0")
                .eq(Goods::getSellStatus,"0")
                .eq(Goods::getStatus,"0")
                .eq(Goods::getDeleteStatus,"0")
                .like(StringUtils.isNotEmpty(parm.getKeywords()),Goods::getTitle,parm.getKeywords())
                .orderByDesc(Goods::getCreateTime);
        IPage<Goods> list = goodsService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    //小程序招领和寻物列表
    @GetMapping("/getFindAndClimList")
    public ResultVo getFindAndClimList(WxGoodsList parm){
        //构造分页对象
        IPage<Goods> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //条件查询
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.lambda().eq(Goods::getType,parm.getType())
                .eq(StringUtils.isNotEmpty(parm.getCategoryId()),Goods::getCategoryId,parm.getCategoryId())
                .eq(Goods::getSellStatus,"0")
                .eq(Goods::getStatus,"0")
                .eq(Goods::getDeleteStatus,"0")
                .like(StringUtils.isNotEmpty(parm.getKeywords()),Goods::getTitle,parm.getKeywords())
                .orderByDesc(Goods::getCreateTime);
        IPage<Goods> list = goodsService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    //我的失物、寻物接口
    @GetMapping("/getMyFindOrClaim")
    public ResultVo getMyFindOrClaim(GoodsPage parm){
        //构造分页对象
        IPage<Goods> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.lambda().eq(Goods::getType,parm.getType())
                .eq(Goods::getUserId,parm.getUserId())
                .eq(Goods::getDeleteStatus,"0")
                .orderByDesc(Goods::getCreateTime);
        IPage<Goods> list = goodsService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    //我的收藏
    @GetMapping("/getMyCollect")
    public ResultVo getMyCollect(GoodsPage parm){
        IPage<Goods> list = goodsService.getMyCollect(parm);
        return ResultUtils.success("查询成功",list);
    }

    //招领
    @PostMapping("/hasClaimandFind")
    public ResultVo hasClaimandFind(@RequestBody Goods goods){
        UpdateWrapper<Goods> query = new UpdateWrapper<>();
        query.lambda().set(Goods::getSellStatus,"1")
                .set(Goods::getStatus,"1")
                .eq(Goods::getGoodsId,goods.getGoodsId());
        if(goodsService.update(query)){
            return ResultUtils.success("操作成功!");
        }
        return ResultUtils.error("操作失败!");
    }

    //编辑
    @PostMapping("/edit")
    public ResultVo edit(@RequestBody Goods goods){
        if(goodsService.updateById(goods)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败！");
    }

}
