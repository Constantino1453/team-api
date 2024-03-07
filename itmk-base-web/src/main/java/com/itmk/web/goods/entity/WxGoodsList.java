package com.itmk.web.goods.entity;

import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class WxGoodsList {
    private Long currentPage;
    private Long pageSize;
    private String keywords;
    private String categoryId;
    private String type;
}
