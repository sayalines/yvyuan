package cn.iocoder.yudao.module.promotion.service.article;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.util.*;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category.*;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.promotion.dal.mysql.article.ArticleCategoryMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.*;

/**
 * 文章分类 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class ArticleCategoryServiceImpl implements ArticleCategoryService {

    @Resource
    private ArticleCategoryMapper articleCategoryMapper;

    @Resource
    @Lazy // 延迟加载，解决循环依赖问题
    private ArticleService articleService;

    @Override
    public List<ArticleCategoryDO> getCategoryListByIds(Set<Long> ids) {
        return articleCategoryMapper.selectBatchIds(ids);
    }

    @Override
    public Long createArticleCategory(ArticleCategorySaveReqVO createReqVO) {
        // 校验上级分类的有效性
        validateParentArticleCategory(null, createReqVO.getParentId());
        // 校验名称的唯一性
        validateArticleCategoryNameUnique(null, createReqVO.getParentId(), createReqVO.getName());

        // 插入
        ArticleCategoryDO articleCategory = BeanUtils.toBean(createReqVO, ArticleCategoryDO.class);
        articleCategoryMapper.insert(articleCategory);
        // 返回
        return articleCategory.getId();
    }

    @Override
    public void updateArticleCategory(ArticleCategorySaveReqVO updateReqVO) {
        // 校验存在
        validateArticleCategoryExists(updateReqVO.getId());
        // 校验上级分类的有效性
        validateParentArticleCategory(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验名称的唯一性
        validateArticleCategoryNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());

        // 更新
        ArticleCategoryDO updateObj = BeanUtils.toBean(updateReqVO, ArticleCategoryDO.class);
        articleCategoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteArticleCategory(Long id) {
        // 校验存在
        validateArticleCategoryExists(id);
        // 校验是否有子文章分类
        if (articleCategoryMapper.selectCountByParentId(id) > 0) {
            throw exception(ARTICLE_CATEGORY_EXITS_CHILDREN);
        }
        // 校验是不是存在关联文章
        Long count = articleService.getArticleCountByCategoryId(id);
        if (count > 0) {
            throw exception(ARTICLE_CATEGORY_DELETE_FAIL_HAVE_ARTICLES);
        }
        // 删除
        articleCategoryMapper.deleteById(id);
    }

    private void validateArticleCategoryExists(Long id) {
        if (articleCategoryMapper.selectById(id) == null) {
            throw exception(ARTICLE_CATEGORY_NOT_EXISTS);
        }
    }

    private void validateParentArticleCategory(Long id, Long parentId) {
        if (parentId == null || ArticleCategoryDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父文章分类
        if (Objects.equals(id, parentId)) {
            throw exception(ARTICLE_CATEGORY_PARENT_ERROR);
        }
        // 2. 父文章分类不存在
        ArticleCategoryDO parentArticleCategory = articleCategoryMapper.selectById(parentId);
        if (parentArticleCategory == null) {
            throw exception(ARTICLE_CATEGORY_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父文章分类，如果父文章分类是自己的子文章分类，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentArticleCategory.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(ARTICLE_CATEGORY_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父文章分类
            if (parentId == null || ArticleCategoryDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentArticleCategory = articleCategoryMapper.selectById(parentId);
            if (parentArticleCategory == null) {
                break;
            }
        }
    }

    private void validateArticleCategoryNameUnique(Long id, Long parentId, String name) {
        ArticleCategoryDO articleCategory = articleCategoryMapper.selectByParentIdAndName(parentId, name);
        if (articleCategory == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的文章分类
        if (id == null) {
            throw exception(ARTICLE_CATEGORY_NAME_DUPLICATE);
        }
        if (!Objects.equals(articleCategory.getId(), id)) {
            throw exception(ARTICLE_CATEGORY_NAME_DUPLICATE);
        }
    }

    @Override
    public ArticleCategoryDO getArticleCategory(Long id) {
        return articleCategoryMapper.selectById(id);
    }

    @Override
    public List<ArticleCategoryDO> getArticleCategoryList(ArticleCategoryListReqVO listReqVO) {
        return articleCategoryMapper.selectList(listReqVO);
    }

    @Override
    public List<ArticleCategoryDO> getArticleCategoryListByStatus(Integer status) {
        return articleCategoryMapper.selectListByStatus(status);
    }

    @Override
    public List<ArticleCategoryDO> findCategoryList(Integer status,String orderBy,Long parentId) {
        LambdaQueryWrapper<ArticleCategoryDO> wrapper = new LambdaQueryWrapper<>();
        if (status!=null){
            wrapper = wrapper.eq(ArticleCategoryDO::getStatus,status);
        }
        if (StringUtils.isEmpty(orderBy)){
            orderBy="asc";
        }

        if (orderBy.equalsIgnoreCase("asc")){
            wrapper =wrapper.orderByAsc(ArticleCategoryDO::getSort);
        }else{
            wrapper =wrapper.orderByDesc(ArticleCategoryDO::getSort);
        }
        if (parentId != null) {
            wrapper = wrapper.eq(ArticleCategoryDO::getParentId, parentId);
        }
        return articleCategoryMapper.selectList(wrapper);
    }
}