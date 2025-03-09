package cn.iocoder.yudao.server.controller.pay;

import cn.iocoder.yudao.framework.pay.core.client.PayClient;
import cn.iocoder.yudao.framework.pay.core.client.dto.order.PayOrderRespDTO;
import cn.iocoder.yudao.framework.pay.core.client.dto.refund.PayRefundRespDTO;
import cn.iocoder.yudao.framework.pay.core.enums.order.PayOrderStatusRespEnum;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import cn.iocoder.yudao.module.pay.dal.mysql.order.PayOrderExtensionMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.order.PayOrderMapper;
import cn.iocoder.yudao.module.pay.service.app.PayAppService;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
//import cn.iocoder.yudao.module.pay.service.refund.PayRefundService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderUpdateService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.util.ResponseResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.CHANNEL_NOT_FOUND;

/**
 *回调通知对外接口
 */
@Tag(name = "对外接口")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/api/pay/notify")
public class NotifyController extends BaseController {

    private final static Logger log = LoggerFactory.getLogger(NotifyController.class);

    @Resource
    private PayOrderService orderService;
    @Resource
    private PayChannelService channelService;
//    @Resource
//    private PayRefundService refundService;

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;
    @Resource
    private PayOrderExtensionMapper orderExtensionMapper;
    @Resource
    private PayOrderMapper orderMapper;

    /**
     * 支付渠道的统一【支付】回调
     * @return
     */
    @PostMapping("/order/{channelId}")
    @ResponseBody
    public String notifyOrder(@PathVariable("channelId") Long channelId,
                              @RequestParam(required = false) Map<String, String> params,
                                       @RequestBody(required = false) String body) {
        if (channelId==null){
            channelId = 1L;
        }
        log.info("[notifyOrder][channelId({}) 回调数据({}/{})]", channelId, params, body);
        // 1. 校验支付渠道是否存在
        PayClient payClient = channelService.getPayClient(channelId);
        if (payClient == null) {
            log.error("[notifyCallback][渠道编号({}) 找不到对应的支付客户端]", channelId);
            throw exception(CHANNEL_NOT_FOUND);
        }

        // 2. 解析通知数据
        PayOrderRespDTO notify = payClient.parseOrderNotify(params, body);
        orderService.notifyOrder(channelId, notify);

        //3. 更新交易订单为已支付
        log.info("更新交易订单状态");
        if (PayOrderStatusRespEnum.isSuccess(notify.getStatus())) {
            log.info("更新交易订单状态 开始 ");
            log.info("outTradeNo:{}",notify.getOutTradeNo());
            PayOrderExtensionDO orderExtension = orderExtensionMapper.selectByNo(notify.getOutTradeNo());
            log.info("payOrderId:{}",orderExtension.getOrderId());
            PayOrderDO order = orderMapper.selectById(orderExtension.getOrderId());
            log.info("merchantOrderId:{},payOrderId:{}",order.getMerchantOrderId(),order.getId());
            tradeOrderUpdateService.updateOrderPaid(Long.valueOf(order.getMerchantOrderId()), order.getId());
            log.info("更新交易订单状态 完成");
        }

        return "success";
    }

//    /**
//     * 支付渠道的统一【退款】回调
//     * @return
//     */
//    @PostMapping("/refund/{channelId}")
//    @ResponseBody
//    public String notifyRefund(@PathVariable("channelId") Long channelId,
//                                @RequestParam(required = false) Map<String, String> params,
//                               @RequestBody(required = false) String body) {
//        if (channelId==null){
//            channelId = 1L;
//        }
//        log.info("[notifyRefund][channelId({}) 回调数据({}/{})]", channelId, params, body);
//        // 1. 校验支付渠道是否存在
//        PayClient payClient = channelService.getPayClient(channelId);
//        if (payClient == null) {
//            log.error("[notifyCallback][渠道编号({}) 找不到对应的支付客户端]", channelId);
//            throw exception(CHANNEL_NOT_FOUND);
//        }
//
//        // 2. 解析通知数据
//        PayRefundRespDTO notify = payClient.parseRefundNotify(params, body);
//        refundService.notifyRefund(channelId, notify);
//        return "success";
//    }
}
