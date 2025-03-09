package cn.iocoder.yudao.module.member.dal.mysql.articlevisit;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.articlevisit.ArticleVisitDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.articlevisit.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 文章访问日志 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface ArticleVisitMapper extends BaseMapperX<ArticleVisitDO> {

    default PageResult<ArticleVisitDO> selectPage(ArticleVisitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ArticleVisitDO>()
                .eqIfPresent(ArticleVisitDO::getArticleId, reqVO.getArticleId())
                .eqIfPresent(ArticleVisitDO::getArticleTitle, reqVO.getArticleTitle())
                .eqIfPresent(ArticleVisitDO::getArticleCategoryId, reqVO.getArticleCategoryId())
                .eqIfPresent(ArticleVisitDO::getUserId, reqVO.getUserId())
                .likeIfPresent(ArticleVisitDO::getUserName, reqVO.getUserName())
                .eqIfPresent(ArticleVisitDO::getUserMobile, reqVO.getUserMobile())
                .betweenIfPresent(ArticleVisitDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ArticleVisitDO::getId));
    }

    /**
     * 按月获取关注主题热度分析
     * @return
     */
    List<Map<String,Object>> findListByMonth(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);


}