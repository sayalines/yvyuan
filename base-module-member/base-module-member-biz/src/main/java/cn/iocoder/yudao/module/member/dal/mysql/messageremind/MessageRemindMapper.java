package cn.iocoder.yudao.module.member.dal.mysql.messageremind;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.messageremind.MessageRemindDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.messageremind.vo.*;

/**
 * 会员消息提醒 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface MessageRemindMapper extends BaseMapperX<MessageRemindDO> {

    default PageResult<MessageRemindDO> selectPage(MessageRemindPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MessageRemindDO>()
                .eqIfPresent(MessageRemindDO::getUserId, reqVO.getUserId())
                .eqIfPresent(MessageRemindDO::getOpenid, reqVO.getOpenid())
                .eqIfPresent(MessageRemindDO::getBizType, reqVO.getBizType())
                .eqIfPresent(MessageRemindDO::getBizId, reqVO.getBizId())
                .eqIfPresent(MessageRemindDO::getIsRemind, reqVO.getIsRemind())
                .betweenIfPresent(MessageRemindDO::getRemindTime, reqVO.getRemindTime())
                .betweenIfPresent(MessageRemindDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MessageRemindDO::getId));
    }

}