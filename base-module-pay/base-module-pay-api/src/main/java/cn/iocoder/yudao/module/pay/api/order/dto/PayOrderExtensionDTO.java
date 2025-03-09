package cn.iocoder.yudao.module.pay.api.order.dto;

import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import lombok.Data;

@Data
public class PayOrderExtensionDTO {
    /**
     * 订单拓展编号，数据库自增
     */
    private Long id;
    /**
     * 外部订单号，根据规则生成
     *
     * 调用支付渠道时，使用该字段作为对接的订单号：
     * 1. 微信支付：对应 <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_1.shtml">JSAPI 支付</a> 的 out_trade_no 字段
     * 2. 支付宝支付：对应 <a href="https://opendocs.alipay.com/open/270/105898">电脑网站支付</a> 的 out_trade_no 字段
     *
     * 例如说，P202110132239124200055
     */
    private String no;
    /**
     * 订单号
     *
     */
    private Long orderId;
    /**
     * 渠道编号
     *
     */
    private Long channelId;
    /**
     * 渠道编码
     */
    private String channelCode;
    /**
     * 用户 IP
     */
    private String userIp;
    /**
     * 支付状态
     *
     * 枚举 {@link PayOrderStatusEnum}
     */
    private Integer status;

    /**
     * 调用渠道的错误码
     */
    private String channelErrorCode;
    /**
     * 调用渠道报错时，错误信息
     */
    private String channelErrorMsg;

    /**
     * 支付渠道的同步/异步通知的内容
     *
     */
    private String channelNotifyData;
}
