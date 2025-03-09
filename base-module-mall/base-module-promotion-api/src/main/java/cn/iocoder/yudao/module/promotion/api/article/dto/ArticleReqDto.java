package cn.iocoder.yudao.module.promotion.api.article.dto;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

@Data
public class ArticleReqDto extends PageParam {
    /**
     * 文章ID
     */
    private Long id;
    /**
     * 文章标题
     */
    private String title;

}