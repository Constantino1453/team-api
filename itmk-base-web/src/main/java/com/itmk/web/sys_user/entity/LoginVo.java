package com.itmk.web.sys_user.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class LoginVo {
    private Long userId;
    private String nickName;
    private String token;
    private List<MenuVo> menuList = new ArrayList<>();
    private List<String> codeList = new ArrayList<>();
}