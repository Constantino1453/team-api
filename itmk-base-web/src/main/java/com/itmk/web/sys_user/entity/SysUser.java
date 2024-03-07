package com.itmk.web.sys_user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long userId;
    //姓名
    private String nickName;
    //性别 0：男 1：女
    private String sex;
    //电话
    private String phone;
    //登录账户
    private String username;
    //登录密码
    private String password;
    //状态 0：启用 1：停用
    private String status;
    //是否超级管理员 0：否 1：是
    private String isAdmin;
}
