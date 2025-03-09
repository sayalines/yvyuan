package cn.iocoder.yudao.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Schema(description = "用户 App - 会员用户更新 Request VO")
@Data
public class AppMemberUserUpdateReqVO {

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String nickname;

    @Schema(description = "头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/x.png")
    @URL(message = "头像必须是 URL 格式")
    private String avatar;

    @Schema(description = "用户真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String name;

    @Schema(description = "性别", example = "1")
    private Integer sex;

    @Schema(description = "用户生日", example = "2000-01-01")
    private String birthday;

    @Schema(description = "证件类型", example = "1")
    private Integer idCodeType;

    @Schema(description = "用户身份证号", example = "59874123650412345X")
    private String idCode;

    @Schema(description = "人员身份", example = "1")
    private Integer userType;
}
