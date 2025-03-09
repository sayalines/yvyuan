package cn.iocoder.yudao.module.statistics.controller.admin.member.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 访客信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MemberVisitRespVO {
    @Schema(description = "访客ID")
    @ExcelProperty("访客ID")
    private Long userId;

    @Schema(description = "访客IP")
    @ExcelProperty("访客IP")
    private String userIp;

    @Schema(description = "手机号")
    @ExcelProperty("手机号")
    private String mobile;

    @Schema(description = "头像")
    @ExcelProperty("头像")
    private String avatar;

    @Schema(description = "昵称")
    @ExcelProperty("昵称")
    private String nickname;

    @Schema(description = "性别")
    @ExcelProperty("性别")
    private Integer sex;

    @Schema(description = "出生日期")
    @ExcelProperty("出生日期")
    private LocalDateTime birthday;

    @Schema(description = "年龄")
    @ExcelProperty("年龄")
    private Integer age;

    @Schema(description = "手机型号")
    @ExcelProperty("手机型号")
    private String mobileModel;

    @Schema(description = "邮箱")
    @ExcelProperty("邮箱")
    private String email;

    @Schema(description = "浏览器信息")
    @ExcelProperty("浏览器信息")
    private String browser;

    @Schema(description = "来源")
    @ExcelProperty("来源")
    private String origins;
}
