package cn.iocoder.yudao.module.member.service.community;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.community.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.community.CommunityDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 论坛文章 Service 接口
 *
 * @author 超级管理员
 */
public interface CommunityService {

    /**
     * 创建论坛文章
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCommunity(@Valid CommunitySaveReqVO createReqVO);

    /**
     * 更新论坛文章
     *
     * @param updateReqVO 更新信息
     */
    void updateCommunity(@Valid CommunitySaveReqVO updateReqVO);

    /**
     * 审核论坛文章
     *
     * @param auditReqVO 审核信息
     */
    void auditCommunity(@Valid CommunityAuditReqVO auditReqVO);

    /**
     * 删除论坛文章
     *
     * @param id 编号
     */
    void deleteCommunity(Long id);

    /**
     * 获得论坛文章
     *
     * @param id 编号
     * @return 论坛文章
     */
    CommunityDO getCommunity(Long id);

    /**
     * 获得论坛文章列表
     *
     * @param ids 编号
     * @return 论坛文章
     */
    List<CommunityDO> getCommunityList(Collection<Long> ids);

    /**
     * 获得论坛文章分页
     *
     * @param pageReqVO 分页查询
     * @return 论坛文章分页
     */
    PageResult<CommunityDO> getCommunityPage(CommunityPageReqVO pageReqVO);

}