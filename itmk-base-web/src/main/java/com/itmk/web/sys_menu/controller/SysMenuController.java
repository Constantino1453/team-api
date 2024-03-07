package com.itmk.web.sys_menu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itmk.annotation.Auth;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_menu.entity.MakeMenuTree;
import com.itmk.web.sys_menu.entity.PermissonVo;
import com.itmk.web.sys_menu.entity.SysMenu;
import com.itmk.web.sys_menu.service.SysMenuService;
import com.itmk.web.sys_user.entity.SysUser;
import com.itmk.web.sys_user.service.SysUserService;
import com.itmk.web.user_menu.entity.AssignParm;
import com.itmk.web.user_menu.service.UserMenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@RestController
@RequestMapping("/api/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private UserMenuService userMenuService;

    //新增
    @PostMapping
    @Auth
    public ResultVo add(@RequestBody SysMenu sysMenu) {
        sysMenu.setCreateTime(new Date());
        if (sysMenuService.save(sysMenu)) {
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //编辑
    @PutMapping
    @Auth
    public ResultVo edit(@RequestBody SysMenu sysMenu) {
        if (sysMenuService.updateById(sysMenu)) {
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @DeleteMapping("/{menuId}")
    @Auth
    public ResultVo delete(@PathVariable("menuId") Long menuId) {
        //如果有下级，不能删除
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        query.lambda().eq(SysMenu::getParentId, menuId);
        List<SysMenu> list = sysMenuService.list(query);
        if (list.size() > 0) {
            return ResultUtils.error("该菜单存在下级，不能删除!");
        }
        if (sysMenuService.removeById(menuId)) {
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //列表
    @GetMapping("/list")
    @Auth
    public ResultVo getList() {
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        query.lambda().orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> list = sysMenuService.list(query);
        //组装树数据
        List<SysMenu> menuList = MakeMenuTree.makeTree(list, 0L);
        return ResultUtils.success("查询成功", menuList);
    }

    //获取上级菜单数据
    @GetMapping("/getParent")
    @Auth
    public ResultVo getParent() {
        List<SysMenu> menuList = sysMenuService.getParent();
        return ResultUtils.success("查询成功", menuList);
    }

    //查询菜单树数据
    @Auth
    @GetMapping("/getAssignTree")
    public ResultVo getAssignTree(Long userId, Long assId) {
        //查询当前用户的信息
        SysUser user = sysUserService.getById(userId);
        //判断是否是超级管理员，超级管理员拥有所有的权限
        List<SysMenu> menuList = null;
        if (StringUtils.isNotEmpty(user.getIsAdmin()) && "1".equals(user.getIsAdmin())) {
            menuList = sysMenuService.list();
        } else {
            menuList = sysMenuService.getMenuByUserId(userId);
        }
        //组装菜单树
        List<SysMenu> tree = MakeMenuTree.makeTree(menuList, 0L);
        PermissonVo vo = new PermissonVo();
        vo.setMenuList(tree);
        //查询回显的树数据： 要分配的用户的菜单权限
        SysUser assignUser = sysUserService.getById(assId);
        List<SysMenu> assignMenu = null;
        if(StringUtils.isNotEmpty(assignUser.getIsAdmin()) && "1".equals(assignUser.getIsAdmin())){
            assignMenu = sysMenuService.list();
        }else{
            assignMenu = sysMenuService.getMenuByUserId(assId);
        }
        //组装返回的数据
        List<Long> ids = new ArrayList<>();
        Optional.ofNullable(assignMenu).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null)
                .forEach(item -> {
                    ids.add(item.getMenuId());
                });
        vo.setCheckList(ids.toArray());
        return ResultUtils.success("查询成功", vo);
    }

    //分配菜单保存
    @PostMapping("/assignSave")
    @Auth
    public ResultVo assignSave(@RequestBody AssignParm parm) {
        //判断是否是超级管理员
        SysUser user = sysUserService.getById(parm.getAssId());
        if (user != null && StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")) {
            return ResultUtils.error("当前用户是超级管理员，无需分配菜单!");
        }
        userMenuService.saveMenu(parm);
        return ResultUtils.success("分配菜单成功!");
    }
}
