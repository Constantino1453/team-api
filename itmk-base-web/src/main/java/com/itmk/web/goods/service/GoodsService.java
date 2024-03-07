package com.itmk.web.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.entity.GoodsPage;


/**
 * @Author java实战基地
 * @Version 2383404558
 */
public interface GoodsService extends IService<Goods> {
    IPage<Goods> getMyCollect(GoodsPage parm);
}
