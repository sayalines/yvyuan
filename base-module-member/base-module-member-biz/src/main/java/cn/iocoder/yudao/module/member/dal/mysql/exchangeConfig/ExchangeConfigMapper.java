package cn.iocoder.yudao.module.member.dal.mysql.exchangeConfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfig.ExchangeConfigDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.exchangeConfig.vo.*;

/**
 * 兑换配置 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface ExchangeConfigMapper extends BaseMapperX<ExchangeConfigDO> {

    default PageResult<ExchangeConfigDO> selectPage(ExchangeConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ExchangeConfigDO>()
                .eqIfPresent(ExchangeConfigDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ExchangeConfigDO::getDescription, reqVO.getDescription())
                .eqIfPresent(ExchangeConfigDO::getBizType, reqVO.getBizType())
                .eqIfPresent(ExchangeConfigDO::getPrefix, reqVO.getPrefix())
                .eqIfPresent(ExchangeConfigDO::getRemarks, reqVO.getRemarks())
                .eqIfPresent(ExchangeConfigDO::getBizId, reqVO.getBizId())
                .betweenIfPresent(ExchangeConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ExchangeConfigDO::getId));
    }

}