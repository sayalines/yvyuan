package cn.iocoder.yudao.module.pay.api.order.dto;

import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PayOrderVO {
    /**
     * 订单编号，数据库自增
     */
    private Long id;
    /**
     * 应用编号
     *
     */
    private Long appId;
    /**
     * 渠道编号
     *
     */
    private Long channelId;
    /**
     * 渠道编码
     *
     */
    private String channelCode;

    // ========== 商户相关字段 ==========

    /**
     * 商户订单编号
     *
     * 例如说，内部系统 A 的订单号，需要保证每个 PayAppDO 唯一
     */
    private String merchantOrderId;
    /**
     * 商品标题
     */
    private String subject;
    /**
     * 商品描述信息
     */
    private String body;
    /**
     * 异步通知地址
     */
    private String notifyUrl;

    // ========== 订单相关字段 ==========

    /**
     * 支付金额，单位：分
     */
    private Integer price;
    /**
     * 渠道手续费，单位：百分比
     *
     */
    private Double channelFeeRate;
    /**
     * 渠道手续金额，单位：分
     */
    private Integer channelFeePrice;
    /**
     * 支付状态
     *
     * 枚举 {@link PayOrderStatusEnum}
     */
    private Integer status;
    /**
     * 用户 IP
     */
    private String userIp;
    /**
     * 订单失效时间
     */
    private LocalDateTime expireTime;
    /**
     * 订单支付成功时间
     */
    private LocalDateTime successTime;
    /**
     * 支付成功的订单拓展单编号
     *
     */
    private Long extensionId;
    /**
     * 支付成功的外部订单号
     *
     */
    private String no;

    // ========== 退款相关字段 ==========
    /**
     * 退款总金额，单位：分
     */
    private Integer refundPrice;

    // ========== 渠道相关字段 ==========
    /**
     * 渠道用户编号
     *
     * 例如说，微信 openid、支付宝账号
     */
    private String channelUserId;
    /**
     * 渠道订单号
     */
    private String channelOrderNo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
}
