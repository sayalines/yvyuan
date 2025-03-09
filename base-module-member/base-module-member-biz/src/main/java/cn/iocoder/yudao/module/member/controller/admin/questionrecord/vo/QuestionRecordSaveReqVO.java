package cn.iocoder.yudao.module.member.controller.admin.questionrecord.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 问卷记录新增/修改 Request VO")
@Data
public class QuestionRecordSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15899")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "问卷ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31597")
    @NotNull(message = "问卷ID不能为空")
    private Long questionId;

    @Schema(description = "答案内容")
    private String answer;

    @Schema(description = "答题开始时间")
    private LocalDateTime startTime;

    @Schema(description = "答题结束时间")
    private LocalDateTime endTime;

    @Schema(description = "答题时间")
    private String answerTime;

    @Schema(description = "报告标题")
    private String resultTitle;

    @Schema(description = "报告结果")
    private String result;

    @Schema(description = "报告参数")
    private String resultParam;

    @Schema(description = "统计结果")
    private String resultStatus;

}