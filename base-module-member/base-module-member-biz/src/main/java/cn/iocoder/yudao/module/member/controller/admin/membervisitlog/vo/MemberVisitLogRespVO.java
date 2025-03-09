package cn.iocoder.yudao.module.member.controller.admin.membervisitlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 访客信息日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MemberVisitLogRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2885")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "统计时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("统计时间")
    private LocalDateTime statTime;

    @Schema(description = "用户ID", example = "12310")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "用户IP")
    @ExcelProperty("用户IP")
    private String userIp;

    @Schema(description = "手机型号")
    @ExcelProperty("手机型号")
    private String mobileModel;

    @Schema(description = "第一次访问时间")
    @ExcelProperty("第一次访问时间")
    private LocalDateTime minTime;

    @Schema(description = "最后一次访问时间")
    @ExcelProperty("最后一次访问时间")
    private LocalDateTime maxTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "手机号")
    @ExcelProperty("手机号")
    private String mobile;

    @Schema(description = "微信头像")
    @ExcelProperty("微信头像")
    private String avatar;

    @Schema(description = "微信昵称")
    @ExcelProperty("微信昵称")
    private String nickname;

    @Schema(description = "性别")
    @ExcelProperty("性别")
    private String sex;

    @Schema(description = "出生日期")
    @ExcelProperty("出生日期")
    private LocalDateTime birthday;

    @Schema(description = "年龄")
    @ExcelProperty("年龄")
    private Integer age;

}