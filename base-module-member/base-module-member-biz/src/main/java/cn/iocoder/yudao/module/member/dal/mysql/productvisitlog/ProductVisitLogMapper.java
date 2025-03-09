package cn.iocoder.yudao.module.member.dal.mysql.productvisitlog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.productvisitlog.ProductVisitLogDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.productvisitlog.vo.*;

/**
 * 商品访问日志 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface ProductVisitLogMapper extends BaseMapperX<ProductVisitLogDO> {

    default PageResult<ProductVisitLogDO> selectPage(ProductVisitLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductVisitLogDO>()
                .betweenIfPresent(ProductVisitLogDO::getStatTime, reqVO.getStatTime())
                .eqIfPresent(ProductVisitLogDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ProductVisitLogDO::getSpuId, reqVO.getSpuId())
                .likeIfPresent(ProductVisitLogDO::getSpuName, reqVO.getSpuName())
                .eqIfPresent(ProductVisitLogDO::getHitCount, reqVO.getHitCount())
                .eqIfPresent(ProductVisitLogDO::getCartCount, reqVO.getCartCount())
                .betweenIfPresent(ProductVisitLogDO::getMinTime, reqVO.getMinTime())
                .betweenIfPresent(ProductVisitLogDO::getMaxTime, reqVO.getMaxTime())
                .betweenIfPresent(ProductVisitLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductVisitLogDO::getId));
    }

}