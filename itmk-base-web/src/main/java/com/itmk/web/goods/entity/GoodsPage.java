package com.itmk.web.goods.entity;

import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class GoodsPage {
    private Long currentPage;
    private Long pageSize;
    private String title;
    private String type;
    private String userName;
    private Long userId;
}
