package cn.iocoder.yudao.module.member.dal.mysql.exchangeRecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeRecord.ExchangeRecordDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.exchangeRecord.vo.*;

/**
 * 会员兑换记录 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface ExchangeRecordMapper extends BaseMapperX<ExchangeRecordDO> {

    default PageResult<ExchangeRecordDO> selectPage(ExchangeRecordPageReqVO reqVO, Set<Long> userIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ExchangeRecordDO>()
                .eqIfPresent(ExchangeRecordDO::getUserId, reqVO.getUserId())
                .inIfPresent(ExchangeRecordDO::getUserId, userIds)
                .eqIfPresent(ExchangeRecordDO::getConfigId, reqVO.getConfigId())
                .eqIfPresent(ExchangeRecordDO::getCode, reqVO.getCode())
                .eqIfPresent(ExchangeRecordDO::getBizType, reqVO.getBizType())
                .eqIfPresent(ExchangeRecordDO::getIsUse, reqVO.getIsUse())
                .betweenIfPresent(ExchangeRecordDO::getUseTime, reqVO.getUseTime())
                .betweenIfPresent(ExchangeRecordDO::getActiveTime, reqVO.getActiveTime())
                .betweenIfPresent(ExchangeRecordDO::getValidStartTime, reqVO.getValidStartTime())
                .betweenIfPresent(ExchangeRecordDO::getValidEndTime, reqVO.getValidEndTime())
                .betweenIfPresent(ExchangeRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ExchangeRecordDO::getId));
    }

}