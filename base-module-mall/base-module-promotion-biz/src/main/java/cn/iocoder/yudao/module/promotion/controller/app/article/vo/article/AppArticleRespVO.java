package cn.iocoder.yudao.module.promotion.controller.app.article.vo.article;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "应用 App - 文章 Response VO")
@Data
public class AppArticleRespVO {

    @Schema(description = "文章编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "商城管理系统 - 促销模块")
    private String title;

    @Schema(description = "文章作者", requiredMode = Schema.RequiredMode.REQUIRED, example = "商城管理系统")
    private String author;

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    private Long categoryId;

    @Schema(description = "分类名称", example = "行为疗法")
    private String categoryName;

    @Schema(description = "上级分类编号", example = "0")
    private Long parentId;

    @Schema(description = "图文封面", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/1.png")
    private String picUrl;

    @Schema(description = "文章简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "我是简介")
    private String introduction;

    @Schema(description = "文章内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "我是详细")
    private String description;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "浏览量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer browseCount;

//    @Schema(description = "关联的商品 SPU 编号", example = "1024")
//    private Long spuId;

    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Schema(description = "备注")
    private String remark;

}
