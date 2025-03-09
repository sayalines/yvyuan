package cn.iocoder.yudao.module.statistics.controller.admin.trade.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 成交数据 Request VO")
@Data
@ToString(callSuper = true)
public class TradeDealReqVO {

    @Schema(description = "总成交人数", example = "1024")
    @ExcelProperty(value = "总成交人数")
    private Integer totalUserCount;

    @Schema(description = "新买家人数", example = "1024")
    @ExcelProperty(value = "新买家人数")
    private Integer newUserCount;

    @Schema(description = "退款人数", example = "1024")
    @ExcelProperty(value = "退款人数")
    private Integer refundUserCount;

    @Schema(description = "总成交金额", example = "1024")
    @ExcelProperty(value = "总成交金额")
    private Integer totalDealAmount;

    @Schema(description = "总退款金额", example = "1024")
    @ExcelProperty(value = "总退款金额")
    private Integer totalRefundAmount;

    @Schema(description = "客单价", example = "1024")
    @ExcelProperty(value = "客单价")
    private Integer avgAmount;

    @Schema(description = "复购率")
    @ExcelProperty("复购率")
    private String reBuyRate;
}
