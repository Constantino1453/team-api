package com.itmk.web.user_menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.web.user_menu.entity.AssignParm;
import com.itmk.web.user_menu.entity.UserMenu;
import com.itmk.web.user_menu.mapper.UserMenuMapper;
import com.itmk.web.user_menu.service.UserMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Service
@Transactional
public class UserMenuServiceImpl extends ServiceImpl<UserMenuMapper, UserMenu> implements UserMenuService {
    @Override
    public void saveMenu(AssignParm parm) {
        //删除用户原来的菜单
        QueryWrapper<UserMenu> query = new QueryWrapper<>();
        query.lambda().eq(UserMenu::getUserId,parm.getAssId());
        this.baseMapper.delete(query);
        //重新插入新分配的菜单
        this.baseMapper.saveMenu(parm.getAssId(),parm.getList());
    }
}
