package cn.iocoder.yudao.module.member.controller.admin.community.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "管理后台 - 论坛文章审核 Request VO")
@Data
public class CommunityAuditReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9858")
    private Long id;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "拒绝原因", example = "不喜欢")
    private String reason;

}