package cn.iocoder.yudao.module.member.controller.admin.memberactionlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 访客行为日志新增/修改 Request VO")
@Data
public class MemberActionLogSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4496")
    private Long id;

    @Schema(description = "统计时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "统计时间不能为空")
    private LocalDateTime statTime;

    @Schema(description = "用户ID", example = "9348")
    private Long userId;

    @Schema(description = "访问页面")
    private String title;

    @Schema(description = "访问数", example = "19583")
    private Integer visitCount;

    @Schema(description = "点击数", example = "30034")
    private Integer clickCount;

    @Schema(description = "第一次访问时间")
    private LocalDateTime minTime;

    @Schema(description = "最后一次访问时间")
    private LocalDateTime maxTime;

}