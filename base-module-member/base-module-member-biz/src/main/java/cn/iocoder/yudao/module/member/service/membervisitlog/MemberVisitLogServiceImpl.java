package cn.iocoder.yudao.module.member.service.membervisitlog;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.membervisitlog.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.membervisitlog.MemberVisitLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.membervisitlog.MemberVisitLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 访客信息日志 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class MemberVisitLogServiceImpl implements MemberVisitLogService {

    @Resource
    private MemberVisitLogMapper visitLogMapper;

    @Override
    public Long createVisitLog(MemberVisitLogSaveReqVO createReqVO) {
        // 插入
        MemberVisitLogDO visitLog = BeanUtils.toBean(createReqVO, MemberVisitLogDO.class);
        visitLogMapper.insert(visitLog);
        // 返回
        return visitLog.getId();
    }

    @Override
    public void updateVisitLog(MemberVisitLogSaveReqVO updateReqVO) {
        // 校验存在
        validateVisitLogExists(updateReqVO.getId());
        // 更新
        MemberVisitLogDO updateObj = BeanUtils.toBean(updateReqVO, MemberVisitLogDO.class);
        visitLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteVisitLog(Long id) {
        // 校验存在
        validateVisitLogExists(id);
        // 删除
        visitLogMapper.deleteById(id);
    }

    private void validateVisitLogExists(Long id) {
        if (visitLogMapper.selectById(id) == null) {
            throw exception(VISIT_LOG_NOT_EXISTS);
        }
    }

    @Override
    public MemberVisitLogDO getVisitLog(Long id) {
        return visitLogMapper.selectById(id);
    }

    @Override
    public PageResult<MemberVisitLogDO> getVisitLogPage(MemberVisitLogPageReqVO pageReqVO) {
        return visitLogMapper.selectPage(pageReqVO);
    }

}