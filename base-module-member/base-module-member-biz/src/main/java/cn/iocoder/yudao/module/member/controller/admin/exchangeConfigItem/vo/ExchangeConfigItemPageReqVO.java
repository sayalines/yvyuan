package cn.iocoder.yudao.module.member.controller.admin.exchangeConfigItem.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 兑换配置明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExchangeConfigItemPageReqVO extends PageParam {

    @Schema(description = "配置ID", example = "13339")
    private Long configId;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "业务类型", example = "2")
    private Integer bizType;

    @Schema(description = "物品ID", example = "27889")
    private Long bizId;

    @Schema(description = "总次数", example = "9385")
    private Integer totalCount;

    @Schema(description = "已使用次数", example = "28464")
    private Integer useCount;

    @Schema(description = "有效开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validStartTime;

    @Schema(description = "有效结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validEndTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}