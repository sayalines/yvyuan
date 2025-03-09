package cn.iocoder.yudao.module.member.dal.mysql.membervisitlog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.membervisitlog.MemberVisitLogDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.membervisitlog.vo.*;

/**
 * 访客信息日志 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface MemberVisitLogMapper extends BaseMapperX<MemberVisitLogDO> {

    default PageResult<MemberVisitLogDO> selectPage(MemberVisitLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberVisitLogDO>()
                .betweenIfPresent(MemberVisitLogDO::getStatTime, reqVO.getStatTime())
                .eqIfPresent(MemberVisitLogDO::getUserId, reqVO.getUserId())
                .eqIfPresent(MemberVisitLogDO::getUserIp, reqVO.getUserIp())
                .eqIfPresent(MemberVisitLogDO::getMobileModel, reqVO.getMobileModel())
                .betweenIfPresent(MemberVisitLogDO::getMinTime, reqVO.getMinTime())
                .betweenIfPresent(MemberVisitLogDO::getMaxTime, reqVO.getMaxTime())
                .betweenIfPresent(MemberVisitLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberVisitLogDO::getId));
    }

}