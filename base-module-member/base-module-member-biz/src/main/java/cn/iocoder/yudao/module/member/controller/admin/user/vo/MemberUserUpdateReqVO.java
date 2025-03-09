package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 会员用户更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberUserUpdateReqVO extends MemberUserBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23788")
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "工会编号")
    private Long deptId;

    @Schema(description = "证件号码")
    private String idCode;

    @Schema(description = "证件类型")
    private Integer idCodeType;




}
