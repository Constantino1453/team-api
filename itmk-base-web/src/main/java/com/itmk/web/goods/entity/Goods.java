package com.itmk.web.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
@TableName("goods")
public class Goods {
    @TableId(type = IdType.AUTO)
    private Long goodsId;
    private Long categoryId;
    private Long userId;
    private String type;
    private String title;
    private String categoryName;
    private String introduce;
    private String address;
    private String userName;
    private String phone;
    private String image;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    private String status;
    private String deleteStatus;
    private String sellStatus;
    private String setIndex;
    @TableField(exist = false)
    private Long collectId;
}
