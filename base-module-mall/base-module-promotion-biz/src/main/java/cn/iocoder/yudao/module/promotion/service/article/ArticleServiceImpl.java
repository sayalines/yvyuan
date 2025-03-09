package cn.iocoder.yudao.module.promotion.service.article;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.mp.account.AccountApi;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticlePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.article.vo.article.AppArticlePageReqVO;
import cn.iocoder.yudao.module.promotion.convert.article.ArticleConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import cn.iocoder.yudao.module.promotion.dal.mysql.article.ArticleMapper;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.ARTICLE_CATEGORY_NOT_EXISTS;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.ARTICLE_NOT_EXISTS;

/**
 * 文章管理 Service 实现类
 *
 * @author HUIHUI
 */
@Service
@Validated
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ArticleCategoryService articleCategoryService;
    @Resource
    private AccountApi accountApi;
    @Override
    public Long createArticle(ArticleCreateReqVO createReqVO) {
        // 校验分类存在
        validateArticleCategoryExists(createReqVO.getCategoryId());

        // 插入
        ArticleDO article = ArticleConvert.INSTANCE.convert(createReqVO);
        article.setBrowseCount(0); // 初始浏览量
        if (createReqVO.getPublishDate()==null){
            article.setPublishDate(LocalDateTime.now());
        }else{
            article.setPublishDate(createReqVO.getPublishDate());
        }
        // pc获取当前用户
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
        article.setDeptId(user.getDeptId());
        articleMapper.insert(article);
        // 返回
        return article.getId();
    }

    @Override
    public void updateArticle(ArticleUpdateReqVO updateReqVO) {
        // 校验存在
        validateArticleExists(updateReqVO.getId());
        // 校验分类存在
        validateArticleCategoryExists(updateReqVO.getCategoryId());

        // 更新
        ArticleDO updateObj = ArticleConvert.INSTANCE.convert(updateReqVO);
        if (updateReqVO.getPublishDate()==null){
            updateObj.setPublishDate(LocalDateTime.now());
        }else{
            updateObj.setPublishDate(updateReqVO.getPublishDate());
        }
        articleMapper.updateById(updateObj);
    }

    @Override
    public void updateArticle(ArticleDO data) {
        articleMapper.updateById(data);
    }

    @Override
    public void deleteArticle(Long id) {
        // 校验存在
        validateArticleExists(id);
        // 删除
        articleMapper.deleteById(id);
    }

    private void validateArticleExists(Long id) {
        if (articleMapper.selectById(id) == null) {
            throw exception(ARTICLE_NOT_EXISTS);
        }
    }

    private void validateArticleCategoryExists(Long categoryId) {
        ArticleCategoryDO articleCategory = articleCategoryService.getArticleCategory(categoryId);
        if (articleCategory == null) {
            throw exception(ARTICLE_CATEGORY_NOT_EXISTS);
        }
    }

    @Override
    public ArticleDO getArticle(Long id) {
        return articleMapper.selectById(id);
    }

    @Override
    public PageResult<ArticleDO> getArticlePage(ArticlePageReqVO pageReqVO) {
        List<Long> categoryIds = new ArrayList<>();
        if (pageReqVO.getParentId()!=null){
            List<ArticleCategoryDO> categoryList = articleCategoryService.findCategoryList(CommonStatusEnum.ENABLE.getStatus(), null,pageReqVO.getParentId());
            if(categoryList!=null && categoryList.size()>0){
                for(ArticleCategoryDO dd:categoryList){
                    categoryIds.add(dd.getId());
                }
            }
        }
        pageReqVO.setCategoryIds(categoryIds);
        return articleMapper.selectPage(pageReqVO);
    }

//    @Override
//    public List<ArticleDO> getArticleCategoryListByRecommend(Boolean recommendHot, Boolean recommendBanner) {
//        return articleMapper.selectList(recommendHot, recommendBanner);
//    }

    @Override
    public PageResult<ArticleDO> getArticlePage(AppArticlePageReqVO pageReqVO) {
        return articleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ArticleDO> getArticleByCategoryId(Long categoryId) {
        return articleMapper.selectList(ArticleDO::getCategoryId, categoryId);
    }

    @Override
    public Long getArticleCountByCategoryId(Long categoryId) {
        return articleMapper.selectCount(ArticleDO::getCategoryId, categoryId);
    }

    @Override
    public List<ArticleDO> getEnabledArticleByCategoryId(Long categoryId) {
        return articleMapper.selectList(ArticleDO::getCategoryId, categoryId,ArticleDO::getStatus, CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    public void addArticleBrowseCount(Long id) {
        // 校验文章是否存在
        validateArticleExists(id);
        // 增加浏览次数
        articleMapper.updateBrowseCount(id);
    }

    //按小程序排版来看，前篇和后篇文章逻辑需要互换（现已更换，从wrapper = wrapper.gt(ArticleDO::getId,id);开始更换）
    @Override
    public ArticleDO getPreviousArticle(Long id,Long categoryId, String orderBy) {
        LambdaQueryWrapper<ArticleDO> wrapper = new LambdaQueryWrapper<>();
        if (categoryId!=null){
            wrapper = wrapper.eq(ArticleDO::getCategoryId,categoryId);
        }
        wrapper = wrapper.gt(ArticleDO::getId,id);
        // 根据orderBy参数确定排序方式
        if (StringUtils.isNotEmpty(orderBy)) {
            switch (orderBy) {
                case "1":
                    //发布时间升序，id降序
                    wrapper.orderByAsc(ArticleDO::getPublishDate).orderByDesc(ArticleDO::getId);
                    break;
                case "2":
                    //发布时间升序，id升序
                    wrapper.orderByAsc(ArticleDO::getPublishDate).orderByAsc(ArticleDO::getId);
                    break;
                case "3":
                    //id升序
                    wrapper.orderByAsc(ArticleDO::getId);
                    break;
                default:
                    // 默认情况：排序升序，发布时间升序，id升序
                    wrapper.orderByAsc(ArticleDO::getSort).orderByAsc(ArticleDO::getPublishDate).orderByAsc(ArticleDO::getId);
            }
        } else {
            // 默认情况：排序升序，发布时间升序，id升序
            wrapper.orderByAsc(ArticleDO::getSort).orderByAsc(ArticleDO::getPublishDate).orderByAsc(ArticleDO::getId);
        }

        PageParam pageParam = new PageParam();
        pageParam.setPageNo(1);
        pageParam.setPageSize(1);
        PageResult<ArticleDO> page = articleMapper.selectPage(pageParam,wrapper);
        if (page!=null && page.getList()!=null && page.getList().size()>0){
            return page.getList().get(0);
        }
        return null;
    }

    @Override
    public ArticleDO getNextArticle(Long id,Long categoryId, String orderBy) {
        LambdaQueryWrapper<ArticleDO> wrapper = new LambdaQueryWrapper<>();
        if (categoryId!=null){
            wrapper = wrapper.eq(ArticleDO::getCategoryId,categoryId);
        }
        wrapper =wrapper.lt(ArticleDO::getId,id);
        // 根据orderBy参数确定排序方式
        if (StringUtils.isNotEmpty(orderBy)) {
            switch (orderBy) {
                case "1":
                    // 发布日期降序，id降序
                    wrapper.orderByDesc(ArticleDO::getPublishDate).orderByDesc(ArticleDO::getId);
                    break;
                case "2":
                    // 发布日期降序，id升序
                    wrapper.orderByDesc(ArticleDO::getPublishDate).orderByAsc(ArticleDO::getId);
                    break;
                case "3":
                    // id降序排序
                    wrapper.orderByDesc(ArticleDO::getId);
                    break;
                default:
                    // 默认情况：排序降序，发布时间降序，id降序
                    wrapper.orderByDesc(ArticleDO::getSort).orderByDesc(ArticleDO::getPublishDate).orderByDesc(ArticleDO::getId);
            }
        } else {
            // 默认情况：排序降序，发布时间降序，id降序
            wrapper.orderByDesc(ArticleDO::getSort).orderByDesc(ArticleDO::getPublishDate).orderByDesc(ArticleDO::getId);
        }

        PageParam pageParam = new PageParam();
        pageParam.setPageNo(1);
        pageParam.setPageSize(1);
        PageResult<ArticleDO> page = articleMapper.selectPage(pageParam,wrapper);
        if (page!=null && page.getList()!=null && page.getList().size()>0){
            return page.getList().get(0);
        }
        return null;
    }

    @Override
    public void createLinkEq(Long id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO!=null){
            String pageUrl = "/pages/equipment/info?id="+articleDO.getId();
//                    String pageTitle = activeDO.getTitle();
//                    Boolean isPermanent = true;
//                    activeDO.setShortUrl(accountApi.createShortLink(pageUrl,pageTitle,isPermanent));
            articleDO.setQrcodeUrl(accountApi.createQrCode(pageUrl,430));
            articleMapper.updateById(articleDO);
        }
    }

    @Override
    public void createLinkEx(Long id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO!=null){
            String pageUrl = "/pages/single/single?id="+articleDO.getId();
            articleDO.setQrcodeUrl(accountApi.createQrCode(pageUrl,430));
            articleMapper.updateById(articleDO);
        }
    }

    @Override
    public void createLinkIn(Long id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO!=null){
            String pageUrl = "/pages/newsInfo/newsInfo?id="+articleDO.getId();
            articleDO.setQrcodeUrl(accountApi.createQrCode(pageUrl,430));
            articleMapper.updateById(articleDO);
        }
    }

    @Override
    public void createLinkPo(Long id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO!=null){
            String pageUrl = "/pages/psychologyInfo/psychologyInfo?id="+articleDO.getId();
            articleDO.setQrcodeUrl(accountApi.createQrCode(pageUrl,430));
            articleMapper.updateById(articleDO);
        }
    }
}
