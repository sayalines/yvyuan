package cn.iocoder.yudao.module.promotion.service.article;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category.*;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;

/**
 * 文章分类 Service 接口
 *
 * @author 超级管理员
 */
public interface ArticleCategoryService {

    /**
     * 创建文章分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createArticleCategory(@Valid ArticleCategorySaveReqVO createReqVO);

    /**
     * 更新文章分类
     *
     * @param updateReqVO 更新信息
     */
    void updateArticleCategory(@Valid ArticleCategorySaveReqVO updateReqVO);

    /**
     * 删除文章分类
     *
     * @param id 编号
     */
    void deleteArticleCategory(Long id);

    /**
     * 获得文章分类
     *
     * @param id 编号
     * @return 文章分类
     */
    ArticleCategoryDO getArticleCategory(Long id);

    /**
     * 获得文章分类列表
     *
     * @param listReqVO 查询条件
     * @return 文章分类列表
     */
    List<ArticleCategoryDO> getArticleCategoryList(ArticleCategoryListReqVO listReqVO);

    /**
     * 获得指定状态的文章分类列表
     *
     * @param status 状态
     * @return 文章分类列表
     */
    List<ArticleCategoryDO> getArticleCategoryListByStatus(Integer status);

    /**
     * 获得指定状态的文章分类列表
     *
     * @param status 状态
     * @return 文章分类列表
     */
    List<ArticleCategoryDO> findCategoryList(Integer status, String orderBy, Long parentId);

    /**
     * 获得文章分类列表(联查)
     *
     * @param ids
     * @return 文章分类列表
     */
    List<ArticleCategoryDO> getCategoryListByIds(Set<Long> ids);

}