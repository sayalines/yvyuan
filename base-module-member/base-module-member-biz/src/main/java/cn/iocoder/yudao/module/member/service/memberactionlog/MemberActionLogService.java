package cn.iocoder.yudao.module.member.service.memberactionlog;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.memberactionlog.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.memberactionlog.MemberActionLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 访客行为日志 Service 接口
 *
 * @author 超级管理员
 */
public interface MemberActionLogService {

    /**
     * 创建访客行为日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createActionLog(@Valid MemberActionLogSaveReqVO createReqVO);

    /**
     * 更新访客行为日志
     *
     * @param updateReqVO 更新信息
     */
    void updateActionLog(@Valid MemberActionLogSaveReqVO updateReqVO);

    /**
     * 删除访客行为日志
     *
     * @param id 编号
     */
    void deleteActionLog(Long id);

    /**
     * 获得访客行为日志
     *
     * @param id 编号
     * @return 访客行为日志
     */
    MemberActionLogDO getActionLog(Long id);

    /**
     * 获得访客行为日志分页
     *
     * @param pageReqVO 分页查询
     * @return 访客行为日志分页
     */
    PageResult<MemberActionLogDO> getActionLogPage(MemberActionLogPageReqVO pageReqVO);

}