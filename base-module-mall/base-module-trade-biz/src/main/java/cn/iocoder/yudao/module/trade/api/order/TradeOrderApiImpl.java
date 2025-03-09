package cn.iocoder.yudao.module.trade.api.order;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.trade.api.order.dto.TradeOrderRespDTO;
import cn.iocoder.yudao.module.trade.convert.order.TradeOrderConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.delivery.DeliveryExpressDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.service.delivery.DeliveryExpressService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderQueryService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderUpdateService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 订单 API 接口实现类
 *
 * @author HUIHUI
 */
@Service
@Validated
public class TradeOrderApiImpl implements TradeOrderApi {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;
    @Resource
    private TradeOrderQueryService tradeOrderQueryService;

    @Resource
    private DeliveryExpressService deliveryExpressService;

    @Override
    public List<TradeOrderRespDTO> getOrderList(Collection<Long> ids) {
        List<TradeOrderDO> orderList = tradeOrderQueryService.getOrderList(ids);
        List<TradeOrderRespDTO> resList = new ArrayList<>();
        if (orderList!=null && orderList.size()>0){
            List<DeliveryExpressDO> deliveryExpressList = deliveryExpressService.getDeliveryExpressListByStatus(CommonStatusEnum.ENABLE.getStatus());
            Map<Long,DeliveryExpressDO> deliveryExpressMap = convertMap(deliveryExpressList,DeliveryExpressDO::getId);
            for(TradeOrderDO dd:orderList){
                TradeOrderRespDTO dto = BeanUtils.toBean(dd,TradeOrderRespDTO.class);
                if (dto.getLogisticsId()!=null && deliveryExpressMap.containsKey(dto.getLogisticsId())){
                    DeliveryExpressDO deliveryExpressDO = deliveryExpressMap.get(dto.getLogisticsId());
                    dto.setLogisticsCompany(deliveryExpressDO.getName());
                }
                resList.add(dto);
            }
        }
        return resList;
    }

    @Override
    public TradeOrderRespDTO getOrder(Long id) {
        return TradeOrderConvert.INSTANCE.convert(tradeOrderQueryService.getOrder(id));
    }

    @Override
    public void cancelPaidOrder(Long userId, Long orderId) {
        tradeOrderUpdateService.cancelPaidOrder(userId, orderId);
    }

    @Override
    public void refundOrder(Long payOrderId) {
        tradeOrderUpdateService.refundOrder(payOrderId);
    }

}
