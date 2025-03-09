package cn.iocoder.yudao.module.member.dal.mysql.config;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.config.TaskConfigDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.config.vo.*;

/**
 * 任务配置 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface TaskConfigMapper extends BaseMapperX<TaskConfigDO> {

    default PageResult<TaskConfigDO> selectPage(TaskConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TaskConfigDO>()
                .eqIfPresent(TaskConfigDO::getTitle, reqVO.getTitle())
                .eqIfPresent(TaskConfigDO::getDescription, reqVO.getDescription())
                .eqIfPresent(TaskConfigDO::getLogo, reqVO.getLogo())
                .eqIfPresent(TaskConfigDO::getBizType, reqVO.getBizType())
                .eqIfPresent(TaskConfigDO::getPoint, reqVO.getPoint())
                .eqIfPresent(TaskConfigDO::getLimitType, reqVO.getLimitType())
                .eqIfPresent(TaskConfigDO::getLimitCount, reqVO.getLimitCount())
                .eqIfPresent(TaskConfigDO::getRemarks, reqVO.getRemarks())
                .betweenIfPresent(TaskConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TaskConfigDO::getId));
    }

}