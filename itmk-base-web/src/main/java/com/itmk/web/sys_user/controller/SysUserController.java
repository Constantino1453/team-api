package com.itmk.web.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.itmk.annotation.Auth;
import com.itmk.jwt.JwtUtils;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_menu.entity.SysMenu;
import com.itmk.web.sys_menu.service.SysMenuService;
import com.itmk.web.sys_user.entity.*;
import com.itmk.web.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private JwtUtils jwtUtils;

    //新增
    @Auth
    @PostMapping
    public ResultVo add(@RequestBody SysUser sysUser){
        //密码加密
        String hex = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(hex);
        if(sysUserService.save(sysUser)){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //编辑
    @Auth
    @PutMapping
    public ResultVo edit(@RequestBody SysUser sysUser){
        if(sysUserService.updateById(sysUser)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @Auth
    @DeleteMapping("/{userId}")
    public ResultVo delete(@PathVariable("userId") Long userId){
        if(sysUserService.removeById(userId)){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //列表
    @Auth
    @GetMapping("/getList")
    public ResultVo getList(PageParm parm){
        //构造查询条件
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.lambda().like(StringUtils.isNotEmpty(parm.getNickName()),SysUser::getNickName,parm.getNickName());
        //构造分页对象
        IPage<SysUser> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //查询
        IPage<SysUser> list = sysUserService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    //重置密码
    @Auth
    @PostMapping("/resetPassword")
    public ResultVo resetPassword(@RequestBody SysUser user){
        UpdateWrapper<SysUser> query = new UpdateWrapper<>();
        String hex = DigestUtils.md5DigestAsHex("666666".getBytes());
        query.lambda().set(SysUser::getPassword,hex)
                .eq(SysUser::getUserId,user.getUserId());
        if(sysUserService.update(query)){
            return ResultUtils.success("重置成功!");
        }
        return ResultUtils.error("重置失败!");
    }

    //生成验证码
    @PostMapping("/imageCode")
    public ResultVo imageCode(HttpServletRequest request){
        //生成验证码文字
        String text = defaultKaptcha.createText();
        //保存到session里面
        HttpSession session = request.getSession();
        session.setAttribute("code",text);
        //生成图片,转换为base64
        BufferedImage bufferedImage = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            String base64 = encoder.encode(outputStream.toByteArray());
            String captchaBase64 = "data:image/jpeg;base64," + base64.replaceAll("\r\n", "");
            ResultVo result = new ResultVo("生成成功", 200, captchaBase64);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //登录
    @PostMapping("/login")
    public ResultVo login(HttpServletRequest request, @RequestBody LoginParm parm){
        //验证验证码是否正确
        HttpSession session = request.getSession();
        //从session里面获取code验证码
        String code = (String) session.getAttribute("code");
        if(StringUtils.isEmpty(code)){
            return ResultUtils.error("验证码过期!");
        }
        //前端传递过来的验证码
        String parmCode = parm.getCode();
        if(!parmCode.equals(code)){
            return ResultUtils.error("验证码不正确!");
        }
        //查询用户信息
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.lambda().eq(SysUser::getUsername,parm.getUsername())
                .eq(SysUser::getPassword,DigestUtils.md5DigestAsHex(parm.getPassword().getBytes()));
        SysUser one = sysUserService.getOne(query);
        if(one == null){
            return ResultUtils.error("用户名或密码错误!");
        }
        if(one.getStatus().equals("1")){
            return ResultUtils.error("用户被停用。请联系管理员!");
        }
        //返回数据给前端
        LoginVo vo = new LoginVo();
        vo.setUserId(one.getUserId());
        vo.setNickName(one.getNickName());
        //查询用户菜单和按钮权限字段
        List<SysMenu> menuList = null;
        //是超级管理员:所有的菜单
        if(StringUtils.isNotEmpty(one.getIsAdmin()) && "1".equals(one.getIsAdmin())){
            QueryWrapper<SysMenu> orderquey = new QueryWrapper<>();
            orderquey.lambda().orderByAsc(SysMenu::getOrderNum);
            menuList = sysMenuService.list(orderquey);
        }else{
            menuList = sysMenuService.getMenuByUserId(one.getUserId());
        }
        //设置权限字段
        List<String> collect = Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> StringUtils.isNotEmpty(item.getCode()))
                .map(item -> item.getCode())
                .collect(Collectors.toList());
        vo.setCodeList(collect);
        //获取菜单
        List<MenuVo> menuVos = Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && item.getType().equals("1"))
                .map(item -> new MenuVo(item.getMenuId(), item.getTitle(), item.getPath(), item.getIcon()))
                .collect(Collectors.toList());
        vo.setMenuList(menuVos);
        //生成token
        Map<String,String> map = new HashMap<>();
        map.put("userId",Long.toString(one.getUserId()));
        String token = jwtUtils.generateToken(map);
        vo.setToken(token);
        return ResultUtils.success("登录成功",vo);
    }

    //修改密码
    @Auth
    @PostMapping("/updatePassword")
    public ResultVo updatePassword(@RequestBody UpdatePasswordParm parm){
        //验证原密码是否正确
        SysUser user = sysUserService.getById(parm.getUserId());
        //加密传递进来的原密码
        String digest = DigestUtils.md5DigestAsHex(parm.getOldPassword().getBytes());
        //对比密码
        if(!user.getPassword().equals(digest)){
            return ResultUtils.error("原密码不正确!");
        }
        //更新密码
        UpdateWrapper<SysUser> query = new UpdateWrapper<>();
        query.lambda().set(SysUser::getPassword,DigestUtils.md5DigestAsHex(parm.getPassword().getBytes()))
                .eq(SysUser::getUserId,parm.getUserId());
        if(sysUserService.update(query)){
            return ResultUtils.success("修改成功!");
        }
        return ResultUtils.error("修改失败!");
    }



}
