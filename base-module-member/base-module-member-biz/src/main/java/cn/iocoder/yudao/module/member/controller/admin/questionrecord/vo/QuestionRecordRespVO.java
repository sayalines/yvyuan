package cn.iocoder.yudao.module.member.controller.admin.questionrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 问卷记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class QuestionRecordRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15899")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2287")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "用户昵称")
    @ExcelProperty("用户昵称")
    private String nickname;

    @Schema(description = "手机号")
    @ExcelProperty("手机号")
    private String mobile;

    @Schema(description = "性别")
    @ExcelProperty("性别")
    private String sex;

    @Schema(description = "年龄")
    @ExcelProperty("年龄")
    private String age;

    @Schema(description = "问卷ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31597")
    @ExcelProperty("问卷ID")
    private Long questionId;

    @Schema(description = "量表主题")
    @ExcelProperty("量表主题")
    private String questionName;

    @Schema(description = "答案内容")
    @ExcelProperty("答案内容")
    private String answer;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "答题开始时间")
    @ExcelProperty("答题开始时间")
    private LocalDateTime startTime;

    @Schema(description = "答题结束时间")
    @ExcelProperty("答题结束时间")
    private LocalDateTime endTime;

    @Schema(description = "答题时间")
    @ExcelProperty("答题时间")
    private String answerTime;

    @Schema(description = "报告标题")
    @ExcelProperty("报告标题")
    private String resultTitle;

    @Schema(description = "报告结果")
    @ExcelProperty("报告结果")
    private String result;

    @Schema(description = "报告参数")
    @ExcelProperty("报告参数")
    private String resultParam;

    @Schema(description = "统计结果")
    @ExcelProperty("统计结果")
    private String resultStatus;
}