package cn.iocoder.yudao.module.member.controller.admin.exchangeConfigItem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 兑换配置明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ExchangeConfigItemRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14857")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "配置ID", example = "13339")
    @ExcelProperty("配置ID")
    private Long configId;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("业务类型")
    private Integer bizType;

    @Schema(description = "物品ID", example = "27889")
    @ExcelProperty("物品ID")
    private Long bizId;

    @Schema(description = "总次数", example = "9385")
    @ExcelProperty("总次数")
    private Integer totalCount;

    @Schema(description = "已使用次数", example = "28464")
    @ExcelProperty("已使用次数")
    private Integer useCount;

    @Schema(description = "有效开始时间")
    @ExcelProperty("有效开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "有效结束时间")
    @ExcelProperty("有效结束时间")
    private LocalDateTime validEndTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}