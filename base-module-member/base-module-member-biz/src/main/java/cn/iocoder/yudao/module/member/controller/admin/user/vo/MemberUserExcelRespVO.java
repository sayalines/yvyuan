package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - 会员用户 Response VO")
@Data
@ToString(callSuper = true)
public class MemberUserExcelRespVO {

    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long id;

    @Schema(description = "注册IP")
    @ExcelProperty(value = "注册IP")
    private String registerIp;

    @Schema(description = "最后登录IP")
    @ExcelProperty(value = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    @ExcelProperty(value = "最后登录时间")
    private String loginDate;

    @Schema(description = "创建时间")
    @ExcelProperty(value = "创建时间")
    private String createTime;

    @Schema(description = "手机号")
    @ExcelProperty(value = "手机号")
    private String mobile;

    @Schema(description = "用户昵称")
    @ExcelProperty(value = "用户昵称")
    private String nickname;

    @Schema(description = "头像")
    @ExcelProperty(value = "头像")
    private String avatar;

    @Schema(description = "用户性别")
    @ExcelProperty(value = "用户性别")
    private String sex;

    @Schema(description = "姓名")
    @ExcelProperty(value = "姓名")
    private String name;

    @Schema(description = "年龄")
    @ExcelProperty(value = "年龄")
    private String age;

    @Schema(description = "所在地")
    @ExcelProperty(value = "所在地")
    private String areaName;

    @Schema(description = "证件类型")
    @ExcelProperty(value = "证件类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.WEIFAN_CARD_TYPE)
    private Integer idCodeType;

    @Schema(description = "证件号码")
    @ExcelProperty(value = "证件号码")
    private String idCode;

    @Schema(description = "用户类型")
    @ExcelProperty(value = "用户类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.WEIFAN_USER_TYPE)
    private Integer userType;

    @Schema(description = "工会ID")
    @ExcelProperty(value = "工会ID")
    private Long deptId;

    @Schema(description = "工会名称")
    @ExcelProperty(value = "工会名称")
    private String deptName;

//    @Schema(description = "出生日期")
//    @ExcelProperty(value = "出生日期")
//    private String birthday;

//    @Schema(description = "会员备注")
//    @ExcelProperty(value = "会员备注")
//    private String mark;

//    @Schema(description = "是否领取新人礼包")
//    @ExcelProperty(value = "是否领取新人礼包")
//    private String isTakeNewGiftPack;
//
//    // ========== 其它信息 ==========
//    @Schema(description = "会员等级")
//    @ExcelProperty(value = "会员等级")
//    private String levelName;
//
//    @Schema(description = "用户分组")
//    @ExcelProperty(value = "用户分组")
//    private String groupName;
//
//    @Schema(description = "积分")
//    @ExcelProperty(value = "积分")
//    private Integer point;
//
//    @Schema(description = "彩虹值")
//    @ExcelProperty(value = "彩虹值")
//    private Integer rainbow;
//
//
//    @Schema(description = "用户经验值")
//    @ExcelProperty(value = "用户经验值")
//    private Integer experience;
//
//    @Schema(description = "openid")
//    @ExcelProperty(value = "openid")
//    private String openid;
//
//    @Schema(description = "unionid")
//    @ExcelProperty(value = "unionid")
//    private String unionid;

}
