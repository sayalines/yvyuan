package cn.iocoder.yudao.module.member.service.community;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.controller.admin.community.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.community.CommunityDO;
import cn.iocoder.yudao.module.member.dal.mysql.community.CommunityMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 论坛文章 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class CommunityServiceImpl implements CommunityService {

    @Resource
    private CommunityMapper communityMapper;

    @Override
    public Long createCommunity(CommunitySaveReqVO createReqVO) {
        createReqVO.setUserId(getLoginUserId());
        System.out.println("createReqVO.getUserId() = " + getLoginUserId());
        CommunityDO community = BeanUtils.toBean(createReqVO, CommunityDO.class);
        System.out.println("community.getUserId() = " + getLoginUserId());
        System.out.println("community.getUserId() = " + community.getUserId());
        communityMapper.insert(community);
        return community.getId();
    }

    @Override
    public void updateCommunity(CommunitySaveReqVO updateReqVO) {
        // 校验存在
        validateCommunityExists(updateReqVO.getId());
        // 更新
        CommunityDO updateObj = BeanUtils.toBean(updateReqVO, CommunityDO.class);
        communityMapper.updateById(updateObj);
    }

    @Override
    public void auditCommunity(CommunityAuditReqVO auditReqVO) {
        CommunityDO communityDO = communityMapper.selectById(auditReqVO.getId());
        if (communityDO==null){
            throw exception(COMMUNITY_NOT_EXISTS);
        }

        if (!communityDO.getStatus().equals(0)){
            throw exception(new ErrorCode(1001, "该文章已处理，操作失败"));
        }

        communityDO.setStatus(auditReqVO.getStatus());
        communityDO.setReason(auditReqVO.getReason());
        if (communityDO.getStatus().equals(1)){
            communityDO.setAuditTime(LocalDateTime.now());
            communityDO.setAuditorId(getLoginUserId());
        }
        communityMapper.updateById(communityDO);
    }

    @Override
    public void deleteCommunity(Long id) {
        // 校验存在
        validateCommunityExists(id);
        // 删除
        communityMapper.deleteById(id);
    }

    private void validateCommunityExists(Long id) {
        if (communityMapper.selectById(id) == null) {
            throw exception(COMMUNITY_NOT_EXISTS);
        }
    }

    @Override
    public CommunityDO getCommunity(Long id) {
        return communityMapper.selectById(id);
    }

    @Override
    public List<CommunityDO> getCommunityList(Collection<Long> ids) {
        if (ids!=null && ids.size()>0){
            return communityMapper.selectBatchIds(ids);
        }
        return null;
    }

    @Override
    public PageResult<CommunityDO> getCommunityPage(CommunityPageReqVO pageReqVO) {
        return communityMapper.selectPage(pageReqVO);
    }

}