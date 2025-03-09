package cn.iocoder.yudao.module.member.dal.mysql.memberactionlog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.memberactionlog.MemberActionLogDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.memberactionlog.vo.*;

/**
 * 访客行为日志 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface MemberActionLogMapper extends BaseMapperX<MemberActionLogDO> {

    default PageResult<MemberActionLogDO> selectPage(MemberActionLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberActionLogDO>()
                .betweenIfPresent(MemberActionLogDO::getStatTime, reqVO.getStatTime())
                .eqIfPresent(MemberActionLogDO::getUserId, reqVO.getUserId())
                .eqIfPresent(MemberActionLogDO::getTitle, reqVO.getTitle())
                .eqIfPresent(MemberActionLogDO::getVisitCount, reqVO.getVisitCount())
                .eqIfPresent(MemberActionLogDO::getClickCount, reqVO.getClickCount())
                .betweenIfPresent(MemberActionLogDO::getMinTime, reqVO.getMinTime())
                .betweenIfPresent(MemberActionLogDO::getMaxTime, reqVO.getMaxTime())
                .betweenIfPresent(MemberActionLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberActionLogDO::getId));
    }

}