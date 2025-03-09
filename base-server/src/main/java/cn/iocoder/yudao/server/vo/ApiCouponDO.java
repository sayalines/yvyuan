package cn.iocoder.yudao.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠劵
 */
@Data
public class ApiCouponDO {
    @Schema(description = "优惠劵编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "优惠劵名", requiredMode = Schema.RequiredMode.REQUIRED, example = "春节送送送")
    private String name;

    @Schema(description = "优惠劵状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1") // 参见 CouponStatusEnum 枚举
    private Integer status;

    @Schema(description = "是否设置满多少金额可用", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    // 单位：分；0 - 不限制
    private BigDecimal usePrice;

    @Schema(description = "固定日期 - 生效开始时间")
    private String validStartTime;

    @Schema(description = "固定日期 - 生效结束时间")
    private String validEndTime;

    @Schema(description = "优惠类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer discountType;

    @Schema(description = "折扣百分比", example = "80") //  例如说，80% 为 80
    private Integer discountPercent;

    @Schema(description = "优惠金额", example = "10")
    @Min(value = 0, message = "优惠金额需要大于等于 0")
    private BigDecimal discountPrice;

    @Schema(description = "折扣上限", example = "100") // 仅在 discountType 为 PERCENT 使用
    private BigDecimal discountLimitPrice;

}
