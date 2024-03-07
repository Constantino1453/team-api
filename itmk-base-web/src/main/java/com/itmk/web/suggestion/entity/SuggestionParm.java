package com.itmk.web.suggestion.entity;

import lombok.Data;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Data
public class SuggestionParm {
    private Long currentPage;
    private Long pageSize;
    private String content;
}
