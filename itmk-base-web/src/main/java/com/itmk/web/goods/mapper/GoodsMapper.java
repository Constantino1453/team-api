package com.itmk.web.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.web.goods.entity.Goods;
import org.apache.ibatis.annotations.Param;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    IPage<Goods> getMyCollect(IPage<Goods> page, @Param("userId") Long userId);
}
