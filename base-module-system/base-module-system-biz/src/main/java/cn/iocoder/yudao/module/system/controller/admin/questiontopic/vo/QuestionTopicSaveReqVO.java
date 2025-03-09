package cn.iocoder.yudao.module.system.controller.admin.questiontopic.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 量表题目新增/修改 Request VO")
@Data
public class QuestionTopicSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27027")
    private Long id;

    @Schema(description = "问卷ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20761")
    @NotNull(message = "问卷ID不能为空")
    private Long questionId;

    @Schema(description = "题目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "题目编号不能为空")
    private String code;

    @Schema(description = "题目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "题目名称不能为空")
    private String name;

    @Schema(description = "题目类型", example = "1")
    private Integer type;

    @Schema(description = "选项内容")
    private String optContent;

    @Schema(description = "排序号")
    private Integer orderNo;

}