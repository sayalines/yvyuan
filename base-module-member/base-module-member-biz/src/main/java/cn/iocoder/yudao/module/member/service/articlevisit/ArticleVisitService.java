package cn.iocoder.yudao.module.member.service.articlevisit;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.articlevisit.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.articlevisit.ArticleVisitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.apache.ibatis.annotations.Param;

/**
 * 文章访问日志 Service 接口
 *
 * @author 超级管理员
 */
public interface ArticleVisitService {

    /**
     * 创建文章访问日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createArticleVisit(@Valid ArticleVisitSaveReqVO createReqVO);

    /**
     * 更新文章访问日志
     *
     * @param updateReqVO 更新信息
     */
    void updateArticleVisit(@Valid ArticleVisitSaveReqVO updateReqVO);

    /**
     * 删除文章访问日志
     *
     * @param id 编号
     */
    void deleteArticleVisit(Long id);

    /**
     * 获得文章访问日志
     *
     * @param id 编号
     * @return 文章访问日志
     */
    ArticleVisitDO getArticleVisit(Long id);

    /**
     * 获得文章访问日志分页
     *
     * @param pageReqVO 分页查询
     * @return 文章访问日志分页
     */
    PageResult<ArticleVisitDO> getArticleVisitPage(ArticleVisitPageReqVO pageReqVO);
    /**
     * 按月获取关注主题热度分析
     * @return
     */
    List<Map<String,Object>> findListByMonth(LocalDateTime beginTime, LocalDateTime endTime);

}