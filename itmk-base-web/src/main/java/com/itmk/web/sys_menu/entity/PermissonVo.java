package com.itmk.web.sys_menu.entity;

import com.itmk.web.sys_menu.entity.SysMenu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class PermissonVo {
    //当前用户的菜单
    List<SysMenu> menuList = new ArrayList<>();
    //回显的数据：被分配的用户的菜单
    private Object[] checkList;
}