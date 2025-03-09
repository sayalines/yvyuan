package cn.iocoder.yudao.module.member.controller.admin.exchangeConfig.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 兑换配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExchangeConfigPageReqVO extends PageParam {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述", example = "你猜")
    private String description;

    @Schema(description = "业务类型", example = "2")
    private Integer bizType;

    @Schema(description = "使用次数", example = "2")
    private Integer useCount;

    @Schema(description = "编号前缀")
    private String prefix;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "物品ID", example = "14000")
    private Long bizId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "有效开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validStartTime;

    @Schema(description = "有效结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validEndTime;

}