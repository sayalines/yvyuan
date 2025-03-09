package cn.iocoder.yudao.module.member.service.membervisitlog;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.membervisitlog.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.membervisitlog.MemberVisitLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 访客信息日志 Service 接口
 *
 * @author 超级管理员
 */
public interface MemberVisitLogService {

    /**
     * 创建访客信息日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVisitLog(@Valid MemberVisitLogSaveReqVO createReqVO);

    /**
     * 更新访客信息日志
     *
     * @param updateReqVO 更新信息
     */
    void updateVisitLog(@Valid MemberVisitLogSaveReqVO updateReqVO);

    /**
     * 删除访客信息日志
     *
     * @param id 编号
     */
    void deleteVisitLog(Long id);

    /**
     * 获得访客信息日志
     *
     * @param id 编号
     * @return 访客信息日志
     */
    MemberVisitLogDO getVisitLog(Long id);

    /**
     * 获得访客信息日志分页
     *
     * @param pageReqVO 分页查询
     * @return 访客信息日志分页
     */
    PageResult<MemberVisitLogDO> getVisitLogPage(MemberVisitLogPageReqVO pageReqVO);

}