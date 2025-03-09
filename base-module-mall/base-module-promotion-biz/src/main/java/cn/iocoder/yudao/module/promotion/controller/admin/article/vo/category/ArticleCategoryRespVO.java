package cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 文章分类 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ArticleCategoryRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4988")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "上级分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "29951")
    private Long parentId;

    @Schema(description = "上级分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "技术")
    @ExcelProperty("上级分类")
    private String parentName;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "图片地址", example = "https://www.iocoder.cn")
    @ExcelProperty("图片地址")
    private String picUrl;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "排序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序号")
    private Integer sort;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "备注", example = "李四")
    private String remark;

}