package cn.iocoder.yudao.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Data
public class ApiPayOrderDO {
    @Schema(description = "支付订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createTime;
    @Schema(description = "应用编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "应用编号不能为空")
    private Long appId;

    @Schema(description = "渠道编号", example = "2048")
    private Long channelId;

    @Schema(description = "渠道编码", example = "wx_app")
    private String channelCode;

    @Schema(description = "商户订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "888")
    @NotNull(message = "商户订单编号不能为空")
    private String merchantOrderId;

    @Schema(description = "商品标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "土豆")
    @NotNull(message = "商品标题不能为空")
    private String subject;

    @Schema(description = "商品描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "我是土豆")
    @NotNull(message = "商品描述不能为空")
    private String body;

    @Schema(description = "异步通知地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://127.0.0.1:48080/pay/notify")
    @NotNull(message = "异步通知地址不能为空")
    private String notifyUrl;

    @Schema(description = "支付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "支付金额，不能为空")
    private BigDecimal price;

    @Schema(description = "渠道手续费，单位：百分比", example = "10")
    private Double channelFeeRate;

    @Schema(description = "渠道手续金额", example = "100")
    private BigDecimal channelFeePrice;

    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "支付状态不能为空")
    private Integer status;

    @Schema(description = "用户 IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    @NotNull(message = "用户 IP不能为空")
    private String userIp;

    @Schema(description = "订单失效时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单失效时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String expireTime;

    @Schema(description = "订单支付成功时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String successTime;

    @Schema(description = "支付成功的订单拓展单编号", example = "50")
    private Long extensionId;

    @Schema(description = "支付订单号", example = "2048888")
    private String no;

    @Schema(description = "退款总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "退款总金额，不能为空")
    private BigDecimal refundPrice;

    @Schema(description = "渠道用户编号", example = "2048")
    private String channelUserId;

    @Schema(description = "渠道订单号", example = "4096")
    private String channelOrderNo;
}
