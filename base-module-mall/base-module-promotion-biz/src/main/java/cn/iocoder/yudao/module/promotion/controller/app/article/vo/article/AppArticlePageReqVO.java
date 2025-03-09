package cn.iocoder.yudao.module.promotion.controller.app.article.vo.article;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "应用 App - 文章的分页 Request VO")
@Data
public class AppArticlePageReqVO extends PageParam {

    @Schema(description = "分类编号", example = "2048")
    private Long categoryId;

    @Schema(description = "分类名称", example = "行为疗法")
    private String categoryName;

    @Schema(description = "文章标题", example = "行为疗法")
    private String title;

    @Schema(description = "上级分类编号", example = "2048")
    private Long parentId;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "排序", example = "1")
    private String orderBy;

}
