package cn.iocoder.yudao.module.system.controller.admin.questiondimension.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 量表维度新增/修改 Request VO")
@Data
public class QuestionDimensionSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "949")
    private Long id;

    @Schema(description = "量表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26545")
    @NotNull(message = "量表ID不能为空")
    private Long questionId;

    @Schema(description = "主题", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "主题不能为空")
    private String name;

    @Schema(description = "简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "你猜")
    @NotEmpty(message = "简介不能为空")
    private String description;

    @Schema(description = "指导语")
    private String comment;

    @Schema(description = "选择因子")
    private List<Long> factors;

    @Schema(description = "分值计算类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "分值计算类型不能为空")
    private Integer scoreType;

    @Schema(description = "常量值")
    private BigDecimal constantValue;

    @Schema(description = "常模值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "常模值不能为空")
    private BigDecimal constantScore;

    @Schema(description = "常模类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "常模类型不能为空")
    private Integer constantScoreType;

    @Schema(description = "选项内容")
    private String optContent;

    @Schema(description = "关键字内容")
    private String keywordContent;

    @Schema(description = "排序号")
    private Integer orderNo;

    @Schema(description = "是否为总分汇总")
    private Integer isTotal;

    @Schema(description = "是否需要显示")
    private Integer isShow;

}