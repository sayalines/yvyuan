package cn.iocoder.yudao.module.member.dal.mysql.exchangeConfigItem;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfigItem.ExchangeConfigItemDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.exchangeConfigItem.vo.*;

/**
 * 兑换配置明细 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface ExchangeConfigItemMapper extends BaseMapperX<ExchangeConfigItemDO> {

    default PageResult<ExchangeConfigItemDO> selectPage(ExchangeConfigItemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ExchangeConfigItemDO>()
                .eqIfPresent(ExchangeConfigItemDO::getConfigId, reqVO.getConfigId())
                .eqIfPresent(ExchangeConfigItemDO::getCode, reqVO.getCode())
                .eqIfPresent(ExchangeConfigItemDO::getBizType, reqVO.getBizType())
                .eqIfPresent(ExchangeConfigItemDO::getBizId, reqVO.getBizId())
                .eqIfPresent(ExchangeConfigItemDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(ExchangeConfigItemDO::getUseCount, reqVO.getUseCount())
                .betweenIfPresent(ExchangeConfigItemDO::getValidStartTime, reqVO.getValidStartTime())
                .betweenIfPresent(ExchangeConfigItemDO::getValidEndTime, reqVO.getValidEndTime())
                .betweenIfPresent(ExchangeConfigItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ExchangeConfigItemDO::getId));
    }

}