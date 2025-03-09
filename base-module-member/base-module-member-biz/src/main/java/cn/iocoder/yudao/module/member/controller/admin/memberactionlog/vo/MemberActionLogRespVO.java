package cn.iocoder.yudao.module.member.controller.admin.memberactionlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 访客行为日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MemberActionLogRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4496")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "统计时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("统计时间")
    private LocalDateTime statTime;

    @Schema(description = "用户ID", example = "9348")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "微信昵称")
    @ExcelProperty("微信昵称")
    private String nickname;

    @Schema(description = "访问页面")
    @ExcelProperty("访问页面")
    private String title;

    @Schema(description = "访问数", example = "19583")
    @ExcelProperty("访问数")
    private Integer visitCount;

    @Schema(description = "点击数", example = "30034")
    @ExcelProperty("点击数")
    private Integer clickCount;

    @Schema(description = "第一次访问时间")
    @ExcelProperty("第一次访问时间")
    private LocalDateTime minTime;

    @Schema(description = "最后一次访问时间")
    @ExcelProperty("最后一次访问时间")
    private LocalDateTime maxTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}