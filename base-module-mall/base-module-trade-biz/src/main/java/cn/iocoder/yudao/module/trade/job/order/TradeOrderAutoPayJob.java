package cn.iocoder.yudao.module.trade.job.order;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderUpdateService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 交易订单的自动支付Job
 *查看是否存在已支付，但订单为已取消的订单；存在的话就自动设置为已支付状态
 * @author 商城管理系统
 */
@Component
public class TradeOrderAutoPayJob implements JobHandler {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;

    @Override
    @TenantJob
    public String execute(String param) {
        int count = tradeOrderUpdateService.payOrderBySystem();
        return String.format("系统自动支付 %s 个", count);
    }

}
