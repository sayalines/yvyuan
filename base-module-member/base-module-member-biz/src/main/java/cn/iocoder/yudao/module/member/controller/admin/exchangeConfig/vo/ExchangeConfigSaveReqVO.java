package cn.iocoder.yudao.module.member.controller.admin.exchangeConfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 兑换配置新增/修改 Request VO")
@Data
public class ExchangeConfigSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21111")
    private Long id;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "描述", example = "你猜")
    private String description;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "业务类型不能为空")
    private Integer bizType;

    @Schema(description = "使用次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "使用次数不能为空")
    private Integer useCount;

    @Schema(description = "编号前缀")
    private String prefix;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "物品ID", example = "14000")
    private Long bizId;

    @Schema(description = "有效开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "有效结束时间")
    private LocalDateTime validEndTime;

    @Schema(description = "兑换码长度")
    private Integer codeLength;

    @Schema(description = "兑换上限")
    private Integer dayCount;

    @Schema(description = "黑名单用户")
    private String blackUsers;

    @Schema(description = "白名单用户")
    private String whiteUsers;

}