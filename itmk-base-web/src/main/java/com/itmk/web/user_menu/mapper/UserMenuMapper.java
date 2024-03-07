package com.itmk.web.user_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itmk.web.user_menu.entity.UserMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
public interface UserMenuMapper extends BaseMapper<UserMenu> {
    boolean saveMenu(@Param("userId") Long userId, @Param("menuIds") List<Long> menuIds);
}
