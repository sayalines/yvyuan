package cn.iocoder.yudao.module.member.controller.admin.community.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 论坛文章分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommunityPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "9893")
    private Long userId;

    @Schema(description = "品牌ID", example = "32648")
    private Long brandId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "是否热门")
    private Integer isHot;

    @Schema(description = "是否精选")
    private Integer isChoice;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "排序字段")
    private String orderBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}