package com.itmk.web.goods.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.entity.GoodsPage;
import com.itmk.web.goods.mapper.GoodsMapper;
import com.itmk.web.goods.service.GoodsService;
import org.springframework.stereotype.Service;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Override
    public IPage<Goods> getMyCollect(GoodsPage parm) {
        //构造分页对象
        IPage<Goods> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        return this.baseMapper.getMyCollect(page,parm.getUserId());
    }
}
