package cn.iocoder.yudao.module.member.controller.admin.articlevisit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 文章访问日志新增/修改 Request VO")
@Data
public class ArticleVisitSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3653")
    private Long id;

    @Schema(description = "文章ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3330")
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @Schema(description = "文章标题")
    private String articleTitle;

    @Schema(description = "文章分类ID")
    private Long articleCategoryId;

    @Schema(description = "文章分类")
    private String articleCategory;

    @Schema(description = "用户ID", example = "479")
    private Long userId;

    @Schema(description = "用户昵称", example = "张三")
    private String userName;

    @Schema(description = "用户手机号")
    private String userMobile;

}