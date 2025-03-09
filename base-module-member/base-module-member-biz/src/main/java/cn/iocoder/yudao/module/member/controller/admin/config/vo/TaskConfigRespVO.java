package cn.iocoder.yudao.module.member.controller.admin.config.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 任务配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TaskConfigRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "8216")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("标题")
    private String title;

    @Schema(description = "描述", example = "你说的对")
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "图标")
    @ExcelProperty("图标")
    private String logo;

    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("业务类型")
    private Integer bizType;

    @Schema(description = "奖励积分", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("奖励积分")
    private Integer point;

    @Schema(description = "限制类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("限制类型")
    private Integer limitType;

    @Schema(description = "限制次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "28850")
    @ExcelProperty("限制次数")
    private Integer limitCount;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remarks;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}