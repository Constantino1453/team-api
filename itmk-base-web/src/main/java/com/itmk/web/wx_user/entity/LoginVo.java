package com.itmk.web.wx_user.entity;

import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class LoginVo {
    private Long userId;
    private String phone;
    private String nickName;
    private String token;
}
