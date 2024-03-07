package com.itmk.web.suggestion.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.annotation.Auth;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.suggestion.entity.Suggestion;
import com.itmk.web.suggestion.entity.SuggestionParm;
import com.itmk.web.suggestion.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@RestController
@RequestMapping("/api/suggestion")
public class SuggestionController {
    @Autowired
    private SuggestionService suggestionService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody Suggestion suggestion){
        suggestion.setCreateTime(new Date());
        if(suggestionService.save(suggestion)){
            return ResultUtils.success("反馈成功!");
        }
        return ResultUtils.error("反馈失败!");
    }

    @Auth
    @GetMapping("/getList")
    public ResultVo getList(SuggestionParm parm){
        //构造分页对象
        IPage<Suggestion> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        QueryWrapper<Suggestion> query = new QueryWrapper<>();
        query.lambda().like(Suggestion::getContent,parm.getContent())
                .orderByDesc(Suggestion::getCreateTime);
        IPage<Suggestion> list = suggestionService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    //删除
    @Auth
    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable("id") Long id){
        if(suggestionService.removeById(id)){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }
}
