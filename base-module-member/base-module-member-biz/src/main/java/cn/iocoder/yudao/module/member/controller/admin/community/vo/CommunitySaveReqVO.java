package cn.iocoder.yudao.module.member.controller.admin.community.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 论坛文章新增/修改 Request VO")
@Data
public class CommunitySaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9858")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9893")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "品牌ID", example = "32648")
    private Long brandId;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "封面图", example = "https://www.iocoder.cn")
    private String picUrl;

    @Schema(description = "附件", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "是否热门")
    private Integer isHot;

    @Schema(description = "是否精选")
    private Integer isChoice;

    @Schema(description = "收藏数", example = "9874")
    private Long collectCount;

    @Schema(description = "点赞数", example = "7718")
    private Long likeCount;

    @Schema(description = "评论数", example = "26224")
    private Long reviewCount;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "拒绝原因", example = "不喜欢")
    private String reason;

}