package cn.iocoder.yudao.module.promotion.dal.mysql.article;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticlePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.article.vo.article.AppArticlePageReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文章管理 Mapper
 *
 * @author HUIHUI
 */
@Mapper
public interface ArticleMapper extends BaseMapperX<ArticleDO> {

    default PageResult<ArticleDO> selectPage(ArticlePageReqVO reqVO) {
        LambdaQueryWrapper<ArticleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(reqVO.getCategoryId()!=null,ArticleDO::getCategoryId, reqVO.getCategoryId())
                .in(reqVO.getCategoryIds()!=null && reqVO.getCategoryIds().size()>0,ArticleDO::getCategoryId, reqVO.getCategoryIds())
                .like(StringUtils.isNotEmpty(reqVO.getTitle()),ArticleDO::getTitle, reqVO.getTitle())
                .eq(StringUtils.isNotEmpty(reqVO.getAuthor()),ArticleDO::getAuthor, reqVO.getAuthor())
                .eq( reqVO.getStatus()!=null,ArticleDO::getStatus, reqVO.getStatus())
                .eq( reqVO.getIfExhibition()!=null,ArticleDO::getIfExhibition, reqVO.getIfExhibition());
//                .eq(reqVO.getRecommendHot()!=null,ArticleDO::getRecommendHot, reqVO.getRecommendHot())
//                .eq(reqVO.getRecommendBanner()!=null,ArticleDO::getRecommendBanner, reqVO.getRecommendBanner());
        if (reqVO.getCreateTime()!=null){
            wrapper =wrapper.between(ArticleDO::getCreateTime,reqVO.getCreateTime()[0],reqVO.getCreateTime()[1]);
        }
        if (StringUtils.isNotEmpty(reqVO.getOrderBy())){
            String orderBy = reqVO.getOrderBy();
            if (orderBy.equalsIgnoreCase("1")){
                wrapper = wrapper.orderByDesc(ArticleDO::getPublishDate, ArticleDO::getId);
            }else if (orderBy.equalsIgnoreCase("2")){
                wrapper = wrapper.orderByAsc(ArticleDO::getPublishDate, ArticleDO::getId);
            }else if (orderBy.equalsIgnoreCase("3")) {
                wrapper = wrapper.orderByDesc(ArticleDO::getId);
            }else{
                wrapper = wrapper.orderByDesc(ArticleDO::getSort, ArticleDO::getPublishDate, ArticleDO::getId);
            }
        }else {
            wrapper = wrapper.orderByDesc(ArticleDO::getSort, ArticleDO::getPublishDate, ArticleDO::getId);
        }
        return selectPage(reqVO, wrapper);
    }

//    default List<ArticleDO> selectList(Boolean recommendHot, Boolean recommendBanner) {
//        return selectList(new LambdaQueryWrapperX<ArticleDO>()
//                .eqIfPresent(ArticleDO::getRecommendHot, recommendHot)
//                .eqIfPresent(ArticleDO::getRecommendBanner, recommendBanner));
//    }

    default PageResult<ArticleDO> selectPage(AppArticlePageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<ArticleDO>()
                .eqIfPresent(ArticleDO::getCategoryId, pageReqVO.getCategoryId()));
    }

    default void updateBrowseCount(Long id) {
        update(null, new LambdaUpdateWrapper<ArticleDO>()
                .eq(ArticleDO::getId, id)
                .setSql("browse_count = browse_count + 1"));
    }

}
