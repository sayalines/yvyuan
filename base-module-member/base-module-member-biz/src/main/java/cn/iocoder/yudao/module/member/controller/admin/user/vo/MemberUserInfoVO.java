package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 会员信息
 */
@Data
public class MemberUserInfoVO {

    @Schema(description = "编号")
    private Long id;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "用户性别")
    private Integer sex;

    @Schema(description = "出生日期")
    private LocalDateTime birthday;

    @Schema(description = "会员等级编号")
    private Long levelId;

    @Schema(description = "用户分组编号")
    private Long groupId;

    @Schema(description = "openid")
    private String openid;

    @Schema(description = "unionid")
    private String unionid;
}
