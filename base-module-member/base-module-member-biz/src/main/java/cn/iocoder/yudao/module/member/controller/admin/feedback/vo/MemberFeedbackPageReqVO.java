package cn.iocoder.yudao.module.member.controller.admin.feedback.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 问题反馈分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberFeedbackPageReqVO extends PageParam {

    @Schema(description = "问题编号", example = "2390")
    private Long id;

    @Schema(description = "用户编号", example = "12630")
    private Long userId;

    @Schema(description = "用户昵称", example = "张三")
    private String userName;

    @Schema(description = "手机号", example = "15320145879")
    private String mobile;

    @Schema(description = "问题内容")
    private String content;

    @Schema(description = "解决状态")
    private Integer ifSolve;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}