package com.itmk.web.user_menu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
@TableName("user_menu")
public class UserMenu {
    @TableId(type = IdType.AUTO)
    private Long userMenuId;
    private Long userId;
    private Long menuId;
}
