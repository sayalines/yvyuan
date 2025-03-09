package cn.iocoder.yudao.module.promotion.api.article;

import cn.iocoder.yudao.module.promotion.api.article.dto.ArticleRespDto;


/**
 * 文章上下篇对app端接口
 */
public interface ArticleApi {

    /**
     * 根据文章ID获取上下篇文章
     *
     * @param id 当前文章ID
     * @return 上下篇文章信息
     */
    ArticleRespDto getArticleNeighbors(Long id);


}