package cn.iocoder.yudao.module.promotion.api.article.dto;

import lombok.Data;

@Data
public class ArticleRespDto {
    /**
     * 上篇文章ID
     */
    private Long previousId;

    /**
     * 上篇文章标题
     */
    private String previousTitle;

    /**
     * 下篇文章ID
     */
    private Long nextId;

    /**
     * 下篇文章标题
     */
    private String nextTitle;
}