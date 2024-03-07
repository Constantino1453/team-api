package com.itmk.web.suggestion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("suggestion")
public class Suggestion {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
