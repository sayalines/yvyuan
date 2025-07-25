package cn.iocoder.yudao.module.promotion.service.article;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticlePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.article.vo.article.AppArticlePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.article.vo.article.AppArticleRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 文章详情 Service 接口
 *
 * @author HUIHUI
 */
public interface ArticleService {

    /**
     * 创建文章详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createArticle(@Valid ArticleCreateReqVO createReqVO);

    /**
     * 更新文章详情
     *
     * @param updateReqVO 更新信息
     */
    void updateArticle(@Valid ArticleUpdateReqVO updateReqVO);

    /**
     * 更新文章详情
     *
     * @param data 更新信息
     */
    void updateArticle(ArticleDO data);

    /**
     * 删除文章详情
     *
     * @param id 编号
     */
    void deleteArticle(Long id);

    /**
     * 获得文章详情
     *
     * @param id 编号
     * @return 文章详情
     */
    ArticleDO getArticle(Long id);

    /**
     * 获得文章详情分页
     *
     * @param pageReqVO 分页查询
     * @return 文章详情分页
     */
    PageResult<ArticleDO> getArticlePage(ArticlePageReqVO pageReqVO);

//    /**
//     * 获得文章详情列表
//     *
//     * @param recommendHot    是否热门
//     * @param recommendBanner 是否轮播图
//     * @return 文章详情列表
//     */
//    List<ArticleDO> getArticleCategoryListByRecommend(Boolean recommendHot, Boolean recommendBanner);

    /**
     * 获得文章详情分页
     *
     * @param pageReqVO 分页查询
     * @return 文章详情分页
     */
    PageResult<ArticleDO> getArticlePage(AppArticlePageReqVO pageReqVO);

    /**
     * 获得指定分类的文章列表
     *
     * @param categoryId 文章分类编号
     * @return 文章列表
     */
    List<ArticleDO> getArticleByCategoryId(Long categoryId);
    /**
     * 获得指定可用分类的文章列表
     *
     * @param categoryId 文章分类编号
     * @return 文章列表
     */
    List<ArticleDO> getEnabledArticleByCategoryId(Long categoryId);

    /**
     * 获得指定分类的文章数量
     *
     * @param categoryId 文章分类编号
     * @return 文章数量
     */
    Long getArticleCountByCategoryId(Long categoryId);

    /**
     * 增加文章浏览量
     *
     * @param id 文章编号
     */
    void addArticleBrowseCount(Long id);

    /**
     * 获得上一篇文章详情
     *
     * @param id 编号
     * @return 文章详情
     */
    ArticleDO getPreviousArticle(Long id,Long categoryId,String orderBy);
    /**
     * 获得下一篇文章详情
     *
     * @param id 编号
     * @return 文章详情
     */
    ArticleDO getNextArticle(Long id,Long categoryId,String orderBy);

    /**
     * 生成链接（设备介绍）
     *
     * @param id 编号
     */
    void createLinkEq(Long id);

    /**
     * 生成链接（拓展资料）
     *
     * @param id 编号
     */
    void createLinkEx(Long id);

    /**
     * 生成链接（资讯）
     *
     * @param id 编号
     */
    void createLinkIn(Long id);

    /**
     * 生成链接（科普知识）
     *
     * @param id 编号
     */
    void createLinkPo(Long id);
}
