package cn.iocoder.yudao.module.member.controller.admin.articlevisit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 文章访问日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ArticleVisitRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3653")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "文章ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3330")
    @ExcelProperty("文章ID")
    private Long articleId;

    @Schema(description = "文章标题")
    @ExcelProperty("文章标题")
    private String articleTitle;

    @Schema(description = "文章分类ID")
    @ExcelProperty("文章分类ID")
    private Long articleCategoryId;

    @Schema(description = "文章分类")
    @ExcelProperty("文章分类")
    private String articleCategory;

    @Schema(description = "用户ID", example = "479")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "用户昵称", example = "张三")
    @ExcelProperty("用户昵称")
    private String userName;

    @Schema(description = "用户手机号")
    @ExcelProperty("用户手机号")
    private String userMobile;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}