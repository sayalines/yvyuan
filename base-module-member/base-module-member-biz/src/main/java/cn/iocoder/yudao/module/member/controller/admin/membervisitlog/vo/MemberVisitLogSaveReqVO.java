package cn.iocoder.yudao.module.member.controller.admin.membervisitlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 访客信息日志新增/修改 Request VO")
@Data
public class MemberVisitLogSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2885")
    private Long id;

    @Schema(description = "统计时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "统计时间不能为空")
    private LocalDateTime statTime;

    @Schema(description = "用户ID", example = "12310")
    private Long userId;

    @Schema(description = "用户IP")
    private String userIp;

    @Schema(description = "手机型号")
    private String mobileModel;

    @Schema(description = "第一次访问时间")
    private LocalDateTime minTime;

    @Schema(description = "最后一次访问时间")
    private LocalDateTime maxTime;

}