package cn.iocoder.yudao.module.member.controller.admin.config.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 任务配置新增/修改 Request VO")
@Data
public class TaskConfigSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "8216")
    private Long id;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "描述", example = "你说的对")
    private String description;

    @Schema(description = "图标")
    private String logo;

    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "业务类型不能为空")
    private Integer bizType;

    @Schema(description = "奖励积分", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "奖励积分不能为空")
    private Integer point;

    @Schema(description = "限制类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "限制类型不能为空")
    private Integer limitType;

    @Schema(description = "限制次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "28850")
    @NotNull(message = "限制次数不能为空")
    private Integer limitCount;

    @Schema(description = "备注")
    private String remarks;

}