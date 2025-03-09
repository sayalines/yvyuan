package cn.iocoder.yudao.module.promotion.api.article;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.promotion.api.article.dto.ArticleRespDto;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import cn.iocoder.yudao.module.promotion.dal.mysql.article.ArticleMapper;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class ArticleApilmpl implements ArticleApi {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public ArticleRespDto getArticleNeighbors(Long id) {
        // 检查文章状态
        checkArticleStatus(id);

        // 获取上一篇文章，即找到当前文章之前的最新文章
        ArticleDO previousArticle = articleMapper.selectOne(
                new LambdaQueryWrapperX<ArticleDO>()
                        .lt(ArticleDO::getId, id) // ID 小于当前文章ID
                        .orderByDesc(ArticleDO::getId) // 按ID降序排列
                        .last("LIMIT 1") // 限制查询结果为1
        );

        // 获取下一篇文章，即找到当前文章之后的最新文章
        ArticleDO nextArticle = articleMapper.selectOne(
                new LambdaQueryWrapperX<ArticleDO>()
                        .gt(ArticleDO::getId, id) // ID 大于当前文章ID
                        .orderByAsc(ArticleDO::getId) // 按ID升序排列
                        .last("LIMIT 1") // 限制查询结果为1
        );

        // 将DO对象转换为DTO对象
        ArticleRespDto respDto = new ArticleRespDto();
        respDto.setPreviousId(previousArticle != null ? previousArticle.getId() : null);
        respDto.setPreviousTitle(previousArticle != null ? previousArticle.getTitle() : "已经到顶啦~");
        respDto.setNextId(nextArticle != null ? nextArticle.getId() : null);
        respDto.setNextTitle(nextArticle != null ? nextArticle.getTitle() : "已经到底啦~");

        return respDto;
    }
    /**
     * 检查文章是否存在且未被删除
     *
     * @param id 文章ID
     */
    private void checkArticleStatus(Long id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO == null || !CommonStatusEnum.isEnable(articleDO.getStatus())) {
            throw ServiceExceptionUtil.exception("文章不存在或已被删除");
        }
    }
}