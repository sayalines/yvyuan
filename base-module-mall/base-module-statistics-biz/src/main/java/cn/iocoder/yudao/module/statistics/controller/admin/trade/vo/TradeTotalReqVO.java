package cn.iocoder.yudao.module.statistics.controller.admin.trade.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 交易汇总 Request VO")
@Data
@ToString(callSuper = true)
public class TradeTotalReqVO {

    @Schema(description = "订单ID")
    @ExcelProperty(value = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    @ExcelProperty(value = "订单编号")
    private String no;

    @Schema(description = "订单类型")
    @ExcelProperty(value = "订单类型")
    private String type;

    @Schema(description = "创建时间")
    @ExcelProperty(value = "创建时间")
    private String createTime;

    @Schema(description = "支付状态")
    @ExcelProperty(value = "支付状态")
    private String payStatus;

    @Schema(description = "订单状态")
    @ExcelProperty(value = "订单状态")
    private String status;

    @Schema(description = "快递公司")
    @ExcelProperty(value = "快递公司")
    private String deliveryName;

    @Schema(description = "快递单号")
    @ExcelProperty(value = "快递单号")
    private String logisticsNo;

    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;

    @Schema(description = "手机号")
    @ExcelProperty(value = "手机号")
    private String mobile;

    @Schema(description = "openid")
    @ExcelProperty(value = "openid")
    private String openid;

    @Schema(description = "unionid")
    @ExcelProperty(value = "unionid")
    private String unionid;

    @Schema(description = "商品数量")
    @ExcelProperty(value = "商品数量")
    private Integer productCount;

    @Schema(description = "总金额")
    @ExcelProperty(value = "总金额")
    private BigDecimal totalPrice;
}
