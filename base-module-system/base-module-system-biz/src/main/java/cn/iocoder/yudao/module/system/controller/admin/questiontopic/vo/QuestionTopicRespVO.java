package cn.iocoder.yudao.module.system.controller.admin.questiontopic.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 量表题目 Response VO")
@Data
@ExcelIgnoreUnannotated
public class QuestionTopicRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27027")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "问卷ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20761")
    @ExcelProperty("问卷ID")
    private Long questionId;

    @Schema(description = "题目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("题目编号")
    private String code;

    @Schema(description = "题目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("题目名称")
    private String name;

    @Schema(description = "题目类型", example = "1")
    @ExcelProperty("题目类型")
    private Integer type;

    @Schema(description = "选项内容")
    @ExcelProperty("选项内容")
    private String optContent;

    @Schema(description = "排序号")
    @ExcelProperty("排序号")
    private Integer orderNo;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "题目名称")
    private String calcName;

}