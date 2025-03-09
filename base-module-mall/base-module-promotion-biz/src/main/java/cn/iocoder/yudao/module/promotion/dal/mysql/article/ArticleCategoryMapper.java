package cn.iocoder.yudao.module.promotion.dal.mysql.article;

import java.util.*;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category.*;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
//import org.springframework.data.repository.query.Param;

/**
 * 文章分类 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface ArticleCategoryMapper extends BaseMapperX<ArticleCategoryDO> {

    default List<ArticleCategoryDO> selectList(ArticleCategoryListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ArticleCategoryDO>()
                .eqIfPresent(ArticleCategoryDO::getParentId, reqVO.getParentId())
                .likeIfPresent(ArticleCategoryDO::getName, reqVO.getName())
                .eqIfPresent(ArticleCategoryDO::getPicUrl, reqVO.getPicUrl())
                .eqIfPresent(ArticleCategoryDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ArticleCategoryDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ArticleCategoryDO::getRemark, reqVO.getRemark())
                .orderByAsc(ArticleCategoryDO::getSort, ArticleCategoryDO::getId));
    }

	default ArticleCategoryDO selectByParentIdAndName(Long parentId, String name) {
	    return selectOne(ArticleCategoryDO::getParentId, parentId, ArticleCategoryDO::getName, name);
	}

    default Long selectCountByParentId(Long parentId) {
        return selectCount(ArticleCategoryDO::getParentId, parentId);
    }

    default List<ArticleCategoryDO> selectListByStatus(Integer status) {
        if (status!=null){
            return selectList(ArticleCategoryDO::getStatus, status);
        }else{
            return selectList();
        }
    }

    List<ArticleCategoryDO> selectCategoryByArticleIds(@Param("ids") List<Long> ids);
}