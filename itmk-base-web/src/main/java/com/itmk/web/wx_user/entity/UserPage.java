package com.itmk.web.wx_user.entity;

import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class UserPage {
    private Long currentPage;
    private Long pageSize;
    private String phone;
}
