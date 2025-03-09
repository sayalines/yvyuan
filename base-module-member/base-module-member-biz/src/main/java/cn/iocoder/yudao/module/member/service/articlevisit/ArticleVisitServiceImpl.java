package cn.iocoder.yudao.module.member.service.articlevisit;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.articlevisit.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.articlevisit.ArticleVisitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.articlevisit.ArticleVisitMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 文章访问日志 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class ArticleVisitServiceImpl implements ArticleVisitService {

    @Resource
    private ArticleVisitMapper articleVisitMapper;

    @Override
    public Long createArticleVisit(ArticleVisitSaveReqVO createReqVO) {
        // 插入
        ArticleVisitDO articleVisit = BeanUtils.toBean(createReqVO, ArticleVisitDO.class);
        articleVisitMapper.insert(articleVisit);
        // 返回
        return articleVisit.getId();
    }

    @Override
    public void updateArticleVisit(ArticleVisitSaveReqVO updateReqVO) {
        // 校验存在
        validateArticleVisitExists(updateReqVO.getId());
        // 更新
        ArticleVisitDO updateObj = BeanUtils.toBean(updateReqVO, ArticleVisitDO.class);
        articleVisitMapper.updateById(updateObj);
    }

    @Override
    public void deleteArticleVisit(Long id) {
        // 校验存在
        validateArticleVisitExists(id);
        // 删除
        articleVisitMapper.deleteById(id);
    }

    private void validateArticleVisitExists(Long id) {
        if (articleVisitMapper.selectById(id) == null) {
            throw exception(ARTICLE_VISIT_NOT_EXISTS);
        }
    }

    @Override
    public ArticleVisitDO getArticleVisit(Long id) {
        return articleVisitMapper.selectById(id);
    }

    @Override
    public PageResult<ArticleVisitDO> getArticleVisitPage(ArticleVisitPageReqVO pageReqVO) {
        return articleVisitMapper.selectPage(pageReqVO);
    }

    @Override
    public List<Map<String, Object>> findListByMonth(LocalDateTime beginTime, LocalDateTime endTime) {
        return articleVisitMapper.findListByMonth(beginTime,endTime);
    }

}