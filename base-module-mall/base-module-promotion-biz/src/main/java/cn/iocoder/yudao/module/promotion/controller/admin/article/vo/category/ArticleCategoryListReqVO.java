package cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 文章分类列表 Request VO")
@Data
public class ArticleCategoryListReqVO {

    @Schema(description = "上级分类", example = "29951")
    private Long parentId;

    @Schema(description = "名称", example = "李四")
    private String name;

    @Schema(description = "图片地址", example = "https://www.iocoder.cn")
    private String picUrl;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "备注")
    private String remark;
}