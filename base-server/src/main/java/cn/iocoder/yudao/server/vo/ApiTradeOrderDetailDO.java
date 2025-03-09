package cn.iocoder.yudao.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ApiTradeOrderDetailDO {
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "订单流水号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1146347329394184195")
    private String no;
    
    @Schema(description = "订单类型")
    private String type;

    @Schema(description = "下单时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createTime;

    @Schema(description = "用户备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "你猜")
    private String userRemark;

    @Schema(description = "订单状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "购买的商品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer productCount;

    @Schema(description = "订单完成时间")
    private String finishTime;

    @Schema(description = "订单取消时间")
    private String cancelTime;

    @Schema(description = "是否评价", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean commentStatus;

    // ========== 价格 + 支付基本信息 ==========

    @Schema(description = "是否已支付", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long payOrderId;

    @Schema(description = "付款时间")
    private String payTime;

    @Schema(description = "付款超时时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String payExpireTime;

    @Schema(description = "支付渠道", example = "wx_lite_pay")
    private String payChannelCode;
    @Schema(description = "支付渠道名", example = "微信小程序支付")
    private String payChannelName;

    @Schema(description = "商品原价（总）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    private BigDecimal totalPrice;

    @Schema(description = "订单优惠（总）", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal discountPrice;

    @Schema(description = "运费金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal deliveryPrice;

    @Schema(description = "订单调价（总）", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal adjustPrice;

    @Schema(description = "应付金额（总）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    private BigDecimal payPrice;

    // ========== 收件 + 物流基本信息 ==========

    @Schema(description = "配送方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer deliveryType;

    @Schema(description = "配送模板编号", example = "10")
    private Long deliveryTemplateId;

    @Schema(description = "发货物流公司编号", example = "10")
    private Long logisticsId;

    @Schema(description = "发货物流名称", example = "顺丰快递")
    private String logisticsName;

    @Schema(description = "发货物流单号", example = "1024")
    private String logisticsNo;

    @Schema(description = "发货时间")
    private String deliveryTime;

    @Schema(description = "收货时间")
    private String receiveTime;

    @Schema(description = "收件人名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String receiverName;

    @Schema(description = "收件人手机", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    private String receiverMobile;

    @Schema(description = "收件人地区编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "110000")
    private Integer receiverAreaId;

    @Schema(description = "收件人地区名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海 上海市 普陀区")
    private String receiverAreaName;

    @Schema(description = "收件人详细地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "中关村大街 1 号")
    private String receiverDetailAddress;

    @Schema(description = "自提门店编号", example = "1088")
    private Long pickUpStoreId;

    @Schema(description = "自提核销码", example = "40964096")
    private String pickUpVerifyCode;

    // ========== 售后基本信息 ==========

    // ========== 营销基本信息 ==========

    @Schema(description = "优惠劵编号", example = "1024")
    private Long couponId;

    @Schema(description = "优惠劵减免金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal couponPrice;

    @Schema(description = "积分抵扣的金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal pointPrice;

    @Schema(description = "赠送的积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer givePoint;

    @Schema(description = "兑换码")
    private String exchangeCode;
    /**
     * 订单项数组
     */
    private List<ApiTradeOrderItem> items;
}
