package cn.iocoder.yudao.module.member.controller.admin.exchangeConfigItem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 兑换配置明细新增/修改 Request VO")
@Data
public class ExchangeConfigItemSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14857")
    private Long id;

    @Schema(description = "配置ID", example = "13339")
    private Long configId;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "业务类型不能为空")
    private Integer bizType;

    @Schema(description = "物品ID", example = "27889")
    private Long bizId;

    @Schema(description = "总次数", example = "9385")
    private Integer totalCount;

    @Schema(description = "已使用次数", example = "28464")
    private Integer useCount;

    @Schema(description = "有效开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "有效结束时间")
    private LocalDateTime validEndTime;

}