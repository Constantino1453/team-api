package com.itmk.web.sys_user.entity;

import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class PageParm {
    private Long currentPage;
    private Long pageSize;
    private String nickName;
}
