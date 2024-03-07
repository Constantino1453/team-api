package com.itmk.web.user_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.web.user_menu.entity.AssignParm;
import com.itmk.web.user_menu.entity.UserMenu;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
public interface UserMenuService extends IService<UserMenu> {
    void saveMenu(AssignParm parm);
}
