package cn.iocoder.yudao.module.member.controller.admin.messageremind.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员消息提醒新增/修改 Request VO")
@Data
public class MessageRemindSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15521")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "8470")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "openID", example = "30870")
    private String openid;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "业务类型不能为空")
    private Integer bizType;

    @Schema(description = "业务ID", example = "4621")
    private Long bizId;

    @Schema(description = "是否已提醒", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否已提醒不能为空")
    private Boolean isRemind;

    @Schema(description = "提醒时间")
    private LocalDateTime remindTime;

}