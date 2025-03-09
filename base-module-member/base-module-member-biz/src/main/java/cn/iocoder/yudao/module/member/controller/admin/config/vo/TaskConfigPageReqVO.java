package cn.iocoder.yudao.module.member.controller.admin.config.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 任务配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskConfigPageReqVO extends PageParam {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述", example = "你说的对")
    private String description;

    @Schema(description = "图标")
    private String logo;

    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "业务类型", example = "2")
    private Integer bizType;

    @Schema(description = "奖励积分")
    private Integer point;

    @Schema(description = "限制类型", example = "1")
    private Integer limitType;

    @Schema(description = "限制次数", example = "28850")
    private Integer limitCount;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}