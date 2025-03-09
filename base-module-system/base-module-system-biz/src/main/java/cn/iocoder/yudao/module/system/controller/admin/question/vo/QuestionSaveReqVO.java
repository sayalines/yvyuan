package cn.iocoder.yudao.module.system.controller.admin.question.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 量表管理新增/修改 Request VO")
@Data
public class QuestionSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12532")
    private Long id;

    @Schema(description = "量表主题", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "量表主题不能为空")
    private String name;

    @Schema(description = "封面图", example = "https://www.iocoder.cn")
    private String picUrl;

    @Schema(description = "简介", example = "你说的对")
    private String description;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "默认评分")
    private BigDecimal score;

    @Schema(description = "题目数")
    private Integer topicCount;

    @Schema(description = "限制时间")
    private Integer limitTime;

    @Schema(description = "使用人数")
    private Integer memberNum;

    @Schema(description = "点击数")
    private String hitCount;

    @Schema(description = "指导语")
    private String comment;

    @Schema(description = "是否允许查看结果")
    private Integer isAnswer;

    @Schema(description = "是否允许查看指导建议")
    private Integer isComment;

    @Schema(description = "是否需要完善会员资料")
    private Integer isMemberinfo;

    @Schema(description = "是否需要图形报表")
    private Integer isGraphic;

    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "重测时间")
    private String resurveyTime;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "因子Y轴最大值")
    private Integer factorMaxAxis;

    @Schema(description = "维度Y轴最大值")
    private Integer dimensionMaxAxis;

    @Schema(description = "总分Y轴最大值")
    private Integer totalMaxAxis;

    @Schema(description = "显示名称")
    private String displayName;

    @Schema(description = "排序号")
    private Integer orderNo;

    @Schema(description = "是否统计")
    private Integer isStat;

}