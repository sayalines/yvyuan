package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 会员用户 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberUserRespVO extends MemberUserBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23788")
    private Long id;

    @Schema(description = "注册 IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    private String registerIp;

    @Schema(description = "最后登录IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    private String loginIp;

    @Schema(description = "最后登录时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime loginDate;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "工会编号",example = "100")
    private Long deptId;

    @Schema(description = "工会名称",example = "潍坊总工会")
    private String deptName;

    @Schema(description = "身份证")
    private String idCode;

    @Schema(description = "身份证类型")
    private Integer idCodeType;

    @Schema(description = "用户类型")
    private Integer userType;


    // ========== 其它信息 ==========

    @Schema(description = "积分", requiredMode  = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer point;

    @Schema(description = "彩虹值", requiredMode  = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer rainbow;

    @Schema(description = "总积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "2000")
    private Integer totalPoint;

    @Schema(description = "会员标签", example = "[红色, 快乐]")
    private List<String> tagNames;

    @Schema(description = "会员等级", example = "黄金会员")
    private String levelName;

    @Schema(description = "用户分组", example = "购物达人")
    private String groupName;

    @Schema(description = "用户经验值", requiredMode  = Schema.RequiredMode.REQUIRED, example = "200")
    private Integer experience;
    /**
     * 是否注册
     */
    @Schema(description = "是否注册")
    private Boolean isRegister;
    /**
     * 是否领取新人礼包
     */
    @Schema(description = "是否领取新人礼包")
    private Boolean isTakeNewGiftPack;

    @Schema(description = "礼包结束时间")
    private LocalDateTime packEndTime;

}
