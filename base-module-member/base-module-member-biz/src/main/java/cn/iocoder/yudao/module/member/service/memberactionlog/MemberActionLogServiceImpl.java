package cn.iocoder.yudao.module.member.service.memberactionlog;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.memberactionlog.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.memberactionlog.MemberActionLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.memberactionlog.MemberActionLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 访客行为日志 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class MemberActionLogServiceImpl implements MemberActionLogService {

    @Resource
    private MemberActionLogMapper actionLogMapper;

    @Override
    public Long createActionLog(MemberActionLogSaveReqVO createReqVO) {
        // 插入
        MemberActionLogDO actionLog = BeanUtils.toBean(createReqVO, MemberActionLogDO.class);
        actionLogMapper.insert(actionLog);
        // 返回
        return actionLog.getId();
    }

    @Override
    public void updateActionLog(MemberActionLogSaveReqVO updateReqVO) {
        // 校验存在
        validateActionLogExists(updateReqVO.getId());
        // 更新
        MemberActionLogDO updateObj = BeanUtils.toBean(updateReqVO, MemberActionLogDO.class);
        actionLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteActionLog(Long id) {
        // 校验存在
        validateActionLogExists(id);
        // 删除
        actionLogMapper.deleteById(id);
    }

    private void validateActionLogExists(Long id) {
        if (actionLogMapper.selectById(id) == null) {
            throw exception(ACTION_LOG_NOT_EXISTS);
        }
    }

    @Override
    public MemberActionLogDO getActionLog(Long id) {
        return actionLogMapper.selectById(id);
    }

    @Override
    public PageResult<MemberActionLogDO> getActionLogPage(MemberActionLogPageReqVO pageReqVO) {
        return actionLogMapper.selectPage(pageReqVO);
    }

}