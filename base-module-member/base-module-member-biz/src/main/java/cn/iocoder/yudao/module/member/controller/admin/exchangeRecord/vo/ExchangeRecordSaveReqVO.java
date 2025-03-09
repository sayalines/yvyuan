package cn.iocoder.yudao.module.member.controller.admin.exchangeRecord.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员兑换记录新增/修改 Request VO")
@Data
public class ExchangeRecordSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23005")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13336")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "配置ID", example = "17866")
    private Long configId;

    @Schema(description = "配置明细ID", example = "17866")
    private Long configItemId;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "业务类型不能为空")
    private Integer bizType;

    @Schema(description = "物品ID", example = "14000")
    private Long bizId;

    @Schema(description = "是否使用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否使用不能为空")
    private Boolean isUse;

    @Schema(description = "使用时间")
    private LocalDateTime useTime;

    @Schema(description = "激活时间")
    private LocalDateTime activeTime;

    @Schema(description = "有效开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "有效结束时间")
    private LocalDateTime validEndTime;

    @Schema(description = "兑换码")
    private String exchangeCode;
}