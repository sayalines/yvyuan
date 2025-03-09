package cn.iocoder.yudao.module.trade.service.order.handler;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.module.product.api.sku.ProductSkuApi;
import cn.iocoder.yudao.module.trade.convert.order.TradeOrderConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * 商品 SKU 库存的 {@link TradeOrderHandler} 实现类
 *
 * @author 商城管理系统
 */
@Component
public class TradeProductSkuOrderHandler implements TradeOrderHandler {

    @Resource
    private ProductSkuApi productSkuApi;

    @Override
    public void beforeOrderCreate(TradeOrderDO order, List<TradeOrderItemDO> orderItems) {
        //扣减库存
        List<TradeOrderItemDO> updateStockList = new ArrayList<>();
        if (orderItems!=null && orderItems.size()>0){
            for(TradeOrderItemDO dd:orderItems){
                updateStockList.add(dd);
            }
        }

        if (updateStockList!=null && updateStockList.size()>0){
            productSkuApi.updateSkuStock(TradeOrderConvert.INSTANCE.convertNegative(updateStockList));
        }
    }

    @Override
    public void afterCancelOrder(TradeOrderDO order, List<TradeOrderItemDO> orderItems) {
        // 售后的订单项，已经在 afterCancelOrderItem 回滚库存，所以这里不需要重复回滚
        orderItems = filterOrderItemListByNoneAfterSale(orderItems);
        if (CollUtil.isEmpty(orderItems)) {
            return;
        }
        //回滚库存
        List<TradeOrderItemDO> updateStockList = new ArrayList<>();
        if (orderItems!=null && orderItems.size()>0){
            for(TradeOrderItemDO dd:orderItems){
                updateStockList.add(dd);
            }
        }
        if (updateStockList!=null && updateStockList.size()>0){
            productSkuApi.updateSkuStock(TradeOrderConvert.INSTANCE.convert(updateStockList));
        }
    }

    @Override
    public void afterCancelOrderItem(TradeOrderDO order, TradeOrderItemDO orderItem) {
        //扣减库存
        List<TradeOrderItemDO> updateStockList = new ArrayList<>();
        if (orderItem!=null){
            updateStockList.add(orderItem);
        }

        if (updateStockList!=null && updateStockList.size()>0){
            productSkuApi.updateSkuStock(TradeOrderConvert.INSTANCE.convert(updateStockList));
        }
    }

}
