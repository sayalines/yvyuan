package cn.iocoder.yudao.module.member.controller.admin.feedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 问题反馈新增/修改 Request VO")
@Data
public class MemberFeedbackSaveReqVO {

    @Schema(description = "问题编号", example = "12")
    private Long id;

    @Schema(description = "用户编号", example = "12")
    private Long userId;

    @Schema(description = "用户昵称", example = "张三")
    private String userName;

    @Schema(description = "手机号", example = "15320145879")
    private String mobile;

    @Schema(description = "问题内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "问题内容不能为空")
    private String content;

    @Schema(description = "解决状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "解决状态不能为空")
    private Integer ifSolve;

}