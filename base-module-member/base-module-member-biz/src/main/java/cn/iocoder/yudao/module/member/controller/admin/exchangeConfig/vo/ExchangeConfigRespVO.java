package cn.iocoder.yudao.module.member.controller.admin.exchangeConfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 兑换配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ExchangeConfigRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21111")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("标题")
    private String title;

    @Schema(description = "描述", example = "你猜")
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("业务类型")
    private Integer bizType;

    @Schema(description = "使用次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("使用次数")
    private Integer useCount;

    @Schema(description = "编号前缀")
    @ExcelProperty("编号前缀")
    private String prefix;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remarks;

    @Schema(description = "物品ID", example = "14000")
    @ExcelProperty("物品ID")
    private Long bizId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "有效开始时间")
    @ExcelProperty("有效开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "有效结束时间")
    @ExcelProperty("有效结束时间")
    private LocalDateTime validEndTime;

    @Schema(description = "兑换码长度")
    @ExcelProperty("兑换码长度")
    private Integer codeLength;

    @Schema(description = "兑换上限")
    @ExcelProperty("兑换上限")
    private Integer dayCount;

    @Schema(description = "黑名单用户")
    @ExcelProperty("黑名单用户")
    private String blackUsers;

    @Schema(description = "白名单用户")
    @ExcelProperty("白名单用户")
    private String whiteUsers;

}