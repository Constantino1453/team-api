package com.itmk.web.user_menu.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class AssignParm {
    private Long assId;
    private List<Long> list;
}