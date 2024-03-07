package com.itmk.web.wx_user.entity;

import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class UpdatePassParm {
    private Long userId;
    private String oldPassword;
    private String password;
}
