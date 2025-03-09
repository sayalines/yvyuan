package cn.iocoder.yudao.module.trade.service.order.handler;

import cn.iocoder.yudao.module.promotion.api.coupon.CouponApi;
import cn.iocoder.yudao.module.promotion.api.coupon.dto.CouponRespDTO;
import cn.iocoder.yudao.module.promotion.api.coupon.dto.CouponUseReqDTO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.COUPON_NOT_EXISTS;

/**
 * 优惠劵的 {@link TradeOrderHandler} 实现类
 *
 * @author 商城管理系统
 */
@Component
public class TradeCouponOrderHandler implements TradeOrderHandler {

    @Resource
    private CouponApi couponApi;

    @Override
    public void afterOrderCreate(TradeOrderDO order, List<TradeOrderItemDO> orderItems) {
        if (order.getCouponId() == null || order.getCouponId() <= 0) {
            return;
        }

        // 不在前置扣减的原因，是因为优惠劵要记录使用的订单号
        couponApi.useCoupon(new CouponUseReqDTO().setId(order.getCouponId()).setUserId(order.getUserId())
                .setOrderId(order.getId()));

        CouponRespDTO coupon = couponApi.getCoupon(order.getCouponId());
        if (coupon==null){
            throw exception(COUPON_NOT_EXISTS);
        }

        if (StringUtils.isNotEmpty(coupon.getExchangeCode())){
//            exchangeRecordApi.useExchangeRecord(order.getUserId(),coupon.getExchangeCode());
        }
    }

    @Override
    public void afterCancelOrder(TradeOrderDO order, List<TradeOrderItemDO> orderItems) {
        if (order.getCouponId() == null || order.getCouponId() <= 0) {
            return;
        }
        // 退回优惠劵
        couponApi.returnUsedCoupon(order.getCouponId());

        CouponRespDTO coupon = couponApi.getCoupon(order.getCouponId());
        if (coupon==null){
            throw exception(COUPON_NOT_EXISTS);
        }

        if (StringUtils.isNotEmpty(coupon.getExchangeCode())){
//            exchangeRecordApi.cancelExchangeRecord(order.getUserId(),coupon.getExchangeCode());
        }
    }

}
