package com.itmk.web.home;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itmk.annotation.Auth;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.service.GoodsService;
import com.itmk.web.suggestion.entity.Suggestion;
import com.itmk.web.suggestion.service.SuggestionService;
import com.itmk.web.wx_user.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@RequestMapping("/api/home")
@RestController
public class HomeController {
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private WxUserService wxUserService;

    //统计总数
    @Auth
    @GetMapping("/getTotal")
    public ResultVo getTotal(){
        TotalVo vo = new TotalVo();
        //查询失物总数
        QueryWrapper<Goods> lostquery = new QueryWrapper<>();
        lostquery.lambda().eq(Goods::getType,"0");
        int lostcount = goodsService.count(lostquery);
        vo.setLostCount(lostcount);
        //查询招领总数
        QueryWrapper<Goods> claimquery = new QueryWrapper<>();
        claimquery.lambda().eq(Goods::getType,"1");
        int claimcount = goodsService.count(claimquery);
        vo.setClaimCount(claimcount);
        //查询会员总数
        int count = wxUserService.count();
        vo.setUserCount(count);
        //反馈总数
        int count1 = suggestionService.count();
        vo.setSuggestionCount(count1);
        return ResultUtils.success("查询成功",vo);
    }

    //查询最近反馈
    @Auth
    @GetMapping("/getList")
    public ResultVo getList(){
        QueryWrapper<Suggestion> query = new QueryWrapper<>();
        query.lambda().orderByDesc(Suggestion::getCreateTime)
                .last(" limit 11");
        List<Suggestion> list = suggestionService.list(query);
        return ResultUtils.success("查询成功",list);
    }
}
