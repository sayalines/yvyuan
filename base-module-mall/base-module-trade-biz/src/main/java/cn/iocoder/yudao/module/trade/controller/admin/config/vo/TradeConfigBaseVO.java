package cn.iocoder.yudao.module.trade.controller.admin.config.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.trade.enums.brokerage.BrokerageBindModeEnum;
import cn.iocoder.yudao.module.trade.enums.brokerage.BrokerageEnabledConditionEnum;
import cn.iocoder.yudao.module.trade.enums.brokerage.BrokerageWithdrawTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * 交易中心配置 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class TradeConfigBaseVO {

    // ========== 售后相关 ==========

    @Schema(description = "售后的退款理由", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> afterSaleRefundReasons;

    @Schema(description = "售后的退货理由", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> afterSaleReturnReasons;

    // ========== 配送相关 ==========

    /**
     * 是否启用全场包邮
     */
    @Schema(description = "是否启用全场包邮", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean deliveryExpressFreeEnabled;

    @Schema(description = "全场包邮的最小金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    @PositiveOrZero(message = "全场包邮的最小金额不能是负数")
    private Integer deliveryExpressFreePrice;

    @Schema(description = "全场包邮的最小数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @PositiveOrZero(message = "全场包邮的最小数量不能是负数")
    private Integer deliveryExpressFreeQty;

    @Schema(description = "是否开启自提", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean deliveryPickUpEnabled;

    // ========== 分销相关 ==========

    @Schema(description = "是否启用分佣", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean brokerageEnabled;

    @Schema(description = "分佣模式", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer brokerageEnabledCondition;

    @Schema(description = "分销关系绑定模式", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer brokerageBindMode;

    @Schema(description = "分销海报图地址数组", requiredMode = Schema.RequiredMode.REQUIRED, example = "[https://www.iocoder.cn/yudao.jpg]")
    private List<String> brokeragePosterUrls;

    @Schema(description = "一级返佣比例", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @Range(min = 0, max = 100, message = "一级返佣比例必须在 0 - 100 之间")
    private Integer brokerageFirstPercent;

    @Schema(description = "二级返佣比例", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @Range(min = 0, max = 100, message = "二级返佣比例必须在 0 - 100 之间")
    private Integer brokerageSecondPercent;

    @Schema(description = "用户提现最低金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    @PositiveOrZero(message = "用户提现最低金额不能是负数")
    private Integer brokerageWithdrawMinPrice;

    @Schema(description = "用户提现手续费百分比", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    @PositiveOrZero(message = "用户提现手续费百分比不能是负数")
    private Integer brokerageWithdrawFeePercent;

    @Schema(description = "提现银行", requiredMode = Schema.RequiredMode.REQUIRED, example = "[0, 1]")
    private List<Integer> brokerageBankNames;

    @Schema(description = "佣金冻结时间(天)", requiredMode = Schema.RequiredMode.REQUIRED, example = "7")
    @PositiveOrZero(message = "佣金冻结时间不能是负数")
    private Integer brokerageFrozenDays;

    @Schema(description = "提现方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "[0, 1]")
    @InEnum(value = BrokerageWithdrawTypeEnum.class, message = "提现方式必须是 {value}")
    private List<Integer> brokerageWithdrawTypes;

}
