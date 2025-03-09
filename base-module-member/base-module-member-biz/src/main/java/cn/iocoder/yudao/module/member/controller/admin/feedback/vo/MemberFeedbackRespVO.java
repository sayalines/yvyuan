package cn.iocoder.yudao.module.member.controller.admin.feedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 问题反馈 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MemberFeedbackRespVO {

    @Schema(description = "问题编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2390")
    @ExcelProperty("问题编号")
    private Long id;

    @Schema(description = "用户编号", example = "12630")
    @ExcelProperty("用户编号")
    private Long userId;

    @Schema(description = "用户昵称", example = "张三")
    @ExcelProperty("用户昵称")
    private String userName;

    @Schema(description = "手机号", example = "15320145879")
    @ExcelProperty("手机号")
    private String mobile;

    @Schema(description = "问题内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("问题内容")
    private String content;

    @Schema(description = "解决状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("解决状态")
    private Integer ifSolve;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}