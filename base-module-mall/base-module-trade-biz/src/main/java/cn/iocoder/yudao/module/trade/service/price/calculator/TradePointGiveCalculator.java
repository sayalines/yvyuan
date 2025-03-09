package cn.iocoder.yudao.module.trade.service.price.calculator;

import cn.hutool.core.util.BooleanUtil;
import cn.iocoder.yudao.framework.common.util.number.MoneyUtils;
import cn.iocoder.yudao.module.member.api.config.MemberConfigApi;
import cn.iocoder.yudao.module.member.api.config.dto.MemberConfigRespDTO;
import cn.iocoder.yudao.module.trade.service.price.bo.TradePriceCalculateReqBO;
import cn.iocoder.yudao.module.trade.service.price.bo.TradePriceCalculateRespBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.filterList;

/**
 * 赠送积分的 {@link TradePriceCalculator} 实现类
 *
 * @author owen
 */
@Component
@Order(TradePriceCalculator.ORDER_POINT_GIVE)
@Slf4j
public class TradePointGiveCalculator implements TradePriceCalculator {

    @Resource
    private MemberConfigApi memberConfigApi;

    @Override
    public void calculate(TradePriceCalculateReqBO param, TradePriceCalculateRespBO result) {
        // 1.1 校验支付金额
        if (result.getPrice().getPayPrice() <= 0) {
            return;
        }

        MemberConfigRespDTO configRespDTO = memberConfigApi.getConfig();
        if (configRespDTO==null){
            return;
        }

        // 1.2 校验积分功能是否开启
        int givePointPerYuan = 0 ;
        if (configRespDTO.getPointTradeDeductEnable()!=null && configRespDTO.getPointTradeDeductEnable()){
            givePointPerYuan = configRespDTO.getPointTradeGivePoint();
        }
        if (givePointPerYuan > 0) {
            // 2.1 计算赠送积分
            int givePoint = MoneyUtils.calculateRatePriceFloor(result.getPrice().getPayPrice(), (double) givePointPerYuan);
            // 2.2 计算分摊的赠送积分
            List<TradePriceCalculateRespBO.OrderItem> orderItems = filterList(result.getItems(), TradePriceCalculateRespBO.OrderItem::getSelected);
            List<Integer> dividePoints = TradePriceCalculatorHelper.dividePrice(orderItems, givePoint);

            // 3.2 更新 SKU 赠送积分
            for (int i = 0; i < orderItems.size(); i++) {
                TradePriceCalculateRespBO.OrderItem orderItem = orderItems.get(i);
                // 商品可能赠送了积分，所以这里要加上
                orderItem.setGivePoint(orderItem.getGivePoint() + dividePoints.get(i));
            }
            // 3.3 更新订单赠送积分
            TradePriceCalculatorHelper.recountAllGivePoint(result);
        }
    }

}
