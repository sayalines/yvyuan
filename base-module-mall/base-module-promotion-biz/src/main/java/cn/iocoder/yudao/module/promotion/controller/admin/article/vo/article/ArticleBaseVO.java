package cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ArticleBaseVO {

    @Schema(description = "文章分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15458")
    @NotNull(message = "文章分类编号不能为空")
    private Long categoryId;

    @Schema(description = "文章分类名称", example = "行为疗法")
    private Long categoryName;

//    @Schema(description = "关联商品编号",example = "22378")
//    private Long spuId;

    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "这是一个标题")
    @NotNull(message = "文章标题不能为空")
    private String title;

    @Schema(description = "文章作者", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String author;

    @Schema(description = "文章封面图片地址", example = "https://www.iocoder.cn")
    private String picUrl;

    @Schema(description = "文章简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "这是一个简介")
    private String introduction;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "是否列表展示", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "是否列表展示不能为空")
    private Integer ifExhibition;

//    @Schema(description = "是否热门(小程序)", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
//    @NotNull(message = "是否热门(小程序)不能为空")
//    private Boolean recommendHot;
//
//    @Schema(description = "是否轮播图(小程序)", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
//    @NotNull(message = "是否轮播图(小程序)不能为空")
//    private Boolean recommendBanner;

    @Schema(description = "文章内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "这是文章内容")
    @NotNull(message = "文章内容不能为空")
    private String content;

    @Schema(description = "发布时间")
    private LocalDateTime publishDate;

    @Schema(description = "标签")
    private String tag;

    @Schema(description = "外链接地址")
    private String href;

    @Schema(description = "多图")
    private List<String> pictures;

    @Schema(description = "小程序码")
    private String qrcodeUrl;

    @Schema(description = "附件")
    private String files;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "备用属性01")
    private String attr01;

    @Schema(description = "备用属性02")
    private String attr02;

    @Schema(description = "备用属性03")
    private String attr03;

    @Schema(description = "备用属性04")
    private String attr04;

    @Schema(description = "备用属性05")
    private String attr05;

    @Schema(description = "点击数")
    private Long hitCount;
}
