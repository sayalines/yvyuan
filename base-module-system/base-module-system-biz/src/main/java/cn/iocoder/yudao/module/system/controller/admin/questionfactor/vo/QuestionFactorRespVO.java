package cn.iocoder.yudao.module.system.controller.admin.questionfactor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 量表因子 Response VO")
@Data
@ExcelIgnoreUnannotated
public class QuestionFactorRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1665")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "量表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "736")
    @ExcelProperty("量表ID")
    private Long questionId;

    @Schema(description = "主题", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("主题")
    private String name;

    @Schema(description = "简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "你说的对")
    @ExcelProperty("简介")
    private String description;

    @Schema(description = "分值计算类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("分值计算类型")
    private Integer scoreType;

    @Schema(description = "排序号")
    @ExcelProperty("排序号")
    private Integer orderNo;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "推荐")
    @ExcelProperty("推荐")
    private String suggest;

    @Schema(description = "选择题目")
    @ExcelProperty("选择题目")
    private List<Long> topics;

    @Schema(description = "常量值")
    @ExcelProperty("常量值")
    private BigDecimal constantValue;

    @Schema(description = "常模值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("常模值")
    private BigDecimal constantScore;

    @Schema(description = "常模类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("常模类型")
    private Integer constantScoreType;

    @Schema(description = "选项内容")
    @ExcelProperty("选项内容")
    private String optContent;

    @Schema(description = "关键字内容")
    @ExcelProperty("选项内容")
    private String keywordContent;

    @Schema(description = "是否为总分汇总")
    @ExcelProperty("是否为总分汇总")
    private Integer isTotal;

    @Schema(description = "是否需要显示")
    @ExcelProperty("是否需要显示")
    private Integer isShow;

}