package cn.iocoder.yudao.module.member.controller.admin.messageremind.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会员消息提醒分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MessageRemindPageReqVO extends PageParam {

    @Schema(description = "用户编号", example = "8470")
    private Long userId;

    @Schema(description = "openID", example = "30870")
    private String openid;

    @Schema(description = "业务类型", example = "2")
    private Integer bizType;

    @Schema(description = "业务ID", example = "4621")
    private Long bizId;

    @Schema(description = "是否已提醒")
    private Boolean isRemind;

    @Schema(description = "提醒时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] remindTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}