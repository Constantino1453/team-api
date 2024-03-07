package com.itmk.web.wx_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.wx_user.entity.*;
import com.itmk.web.wx_user.service.WxUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@RequestMapping("/api/wxUser")
@RestController
public class WxUserController {
    @Autowired
    private WxUserService wxUserService;

    //注册
    @PostMapping("/register")
    public ResultVo register(@RequestBody WxUser wxUser) {
        //判断账户是否被占用
        QueryWrapper<WxUser> query = new QueryWrapper<>();
        query.lambda().eq(WxUser::getUsername, wxUser.getUsername());
        WxUser one = wxUserService.getOne(query);
        if (one != null) {
            return ResultUtils.error("账户被占用!");
        }
        //密码加密
        String pas = DigestUtils.md5DigestAsHex(wxUser.getPassword().getBytes());
        wxUser.setPassword(pas);
        if (wxUserService.save(wxUser)) {
            return ResultUtils.success("注册成功!");
        }
        return ResultUtils.error("注册失败!");
    }

    //登录
    @PostMapping("/login")
    public ResultVo login(@RequestBody WxUser wxUser) {
        //密码加密
        String pas = DigestUtils.md5DigestAsHex(wxUser.getPassword().getBytes());
        //根据用户名密码查询
        QueryWrapper<WxUser> query = new QueryWrapper<>();
        query.lambda().eq(WxUser::getUsername, wxUser.getUsername())
                .eq(WxUser::getPassword, pas);
        WxUser user = wxUserService.getOne(query);
        //判断是否查询到
        if (user == null) {
            return ResultUtils.error("账户或者密码错误!");
        }
        //判断账户是否被停用
        if ("1".equals(user.getStatus())) {
            return ResultUtils.error("账户被停用!");
        }
        LoginVo vo = new LoginVo();
        vo.setUserId(user.getUserId());
        vo.setNickName(user.getNickName());
        vo.setPhone(user.getPhone());
        return ResultUtils.success("登录成功", vo);
    }

    //列表
    @GetMapping("/wxList")
    public ResultVo list(UserPage parm) {
        //构造分页对象
        IPage<WxUser> page = new Page<>(parm.getCurrentPage(), parm.getPageSize());
        //构造查询条件
        QueryWrapper<WxUser> query = new QueryWrapper<>();
        query.lambda().like(StringUtils.isNotEmpty(parm.getPhone()), WxUser::getPhone, parm.getPhone());
        IPage<WxUser> list = wxUserService.page(page, query);
        return ResultUtils.success("查询成功", list);
    }

    //账户停用启用
    @PostMapping("/stopStatus")
    public ResultVo stopStatus(@RequestBody WxUser user) {
        UpdateWrapper<WxUser> query = new UpdateWrapper<>();
        query.lambda().set(WxUser::getStatus, user.getStatus())
                .eq(WxUser::getUserId, user.getUserId());
        if (wxUserService.update(query)) {
            return ResultUtils.success("操作成功!");
        }
        return ResultUtils.error("操作失败!");
    }

    //账户停用启用
    @DeleteMapping("/{userId}")
    public ResultVo delete(@PathVariable("userId") Long userId) {
        if (wxUserService.removeById(userId)) {
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //重置密码
    @PostMapping("/resetPassword")
    public ResultVo resetPassword(@RequestBody WxUser user) {
        UpdateWrapper<WxUser> query = new UpdateWrapper<>();
        String hex = DigestUtils.md5DigestAsHex("666666".getBytes());
        query.lambda().set(WxUser::getPassword, hex)
                .eq(WxUser::getUserId, user.getUserId());
        if (wxUserService.update(query)) {
            return ResultUtils.success("重置成功!");
        }
        return ResultUtils.error("重置失败!");
    }

    //修改密码
    @PostMapping("/updatePass")
    public ResultVo updatePass(@RequestBody UpdatePassParm parm) {
        String pas = DigestUtils.md5DigestAsHex(parm.getOldPassword().getBytes());
        //判断原密码是否正确
        QueryWrapper<WxUser> query = new QueryWrapper<>();
        query.lambda().eq(WxUser::getUserId, parm.getUserId())
                .eq(WxUser::getPassword, DigestUtils.md5DigestAsHex(parm.getOldPassword().getBytes()));
        WxUser one = wxUserService.getOne(query);
        if (one == null) {
            return ResultUtils.error("原密码不正确!");
        }
        UpdateWrapper<WxUser> update = new UpdateWrapper<>();
        update.lambda().set(WxUser::getPassword, DigestUtils.md5DigestAsHex(parm.getPassword().getBytes()))
                .eq(WxUser::getUserId, parm.getUserId());
        if (wxUserService.update(update)) {
            return ResultUtils.success("修改成功!");
        }
        return ResultUtils.error("修改失败!");
    }

    //修改信息
    @PostMapping("/edit")
    public ResultVo edit(@RequestBody WxUser user) {
        if (wxUserService.updateById(user)) {
            return ResultUtils.success("修改成功!");
        }
        return ResultUtils.error("修改失败!");
    }

    //根据id查询用户信息
    @GetMapping("/getInfo")
    public ResultVo getInfo(Long userId){
        WxUser user = wxUserService.getById(userId);
        return ResultUtils.success("查询成功",user);
    }

    //小程序忘记密码
    @PostMapping("/forget")
    public ResultVo forget(@RequestBody ForgetParm parm){
        //判断账户和电话是否正确
        QueryWrapper<WxUser> query = new QueryWrapper<>();
        query.lambda().eq(WxUser::getUsername,parm.getUsername())
                .eq(WxUser::getPhone,parm.getPhone());
        WxUser one = wxUserService.getOne(query);
        if(one == null){
            return ResultUtils.error("账户或者电话号码不正确!");
        }
        //更新操作
        UpdateWrapper<WxUser> update = new UpdateWrapper<>();
        //设置新密码
        update.lambda().set(WxUser::getPassword,DigestUtils.md5DigestAsHex(parm.getPassword().getBytes()))
                .eq(WxUser::getUsername,parm.getUsername())
                .eq(WxUser::getPhone,parm.getPhone());
        if(wxUserService.update(update)){
            return ResultUtils.success("修改成功!");
        }
        return ResultUtils.error("修改失败！");
    }
}
