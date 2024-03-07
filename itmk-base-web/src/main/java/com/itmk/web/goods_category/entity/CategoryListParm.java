package com.itmk.web.goods_category.entity;

import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class CategoryListParm {
    private Long currentPage;
    private Long pageSize;
    private String categoryName;
}
