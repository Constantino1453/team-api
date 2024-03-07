package com.itmk.web.sys_user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
@AllArgsConstructor
public class MenuVo {
    private Long menuId;
    private String title;
    private String path;
    private String icon;
}