package cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 文章管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ArticlePageReqVO extends PageParam {

    @TableId
    @Schema(description = "文章编号", example = "15458")
    private Long id;

    @Schema(description = "文章分类编号", example = "15458")
    private Long categoryId;

    @Schema(description = "多个文章分类编号")
    private List<Long> categoryIds;

    @Schema(description = "文章分类名称", example = "行为疗法")
    private String categoryName;

    @Schema(description = "一级分类编号", example = "0")
    private Long parentId;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章作者")
    private String author;

    @Schema(description = "文章封面图片地址")
    private String picUrl;

    @Schema(description = "内容简介")
    private String introduction;

    @Schema(description = "浏览次数")
    private Integer browseCount;

    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "是否列表显示")
    private Integer ifExhibition;

//    @Schema(description = "是否热门(小程序)")
//    private Boolean recommendHot;
//
//    @Schema(description = "是否轮播图(小程序)")
//    private Boolean recommendBanner;

    @Schema(description = "外链接地址")
    private String href;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "发布时间")
    private String publishDate;

    @Schema(description = "排序方式")
    private String orderBy;

    @Schema(description = "多图")
    private List<String> pictures;

    @Schema(description = "附件")
    private String files;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "备用属性01")
    private String attr01;

    @Schema(description = "备用属性02")
    private String attr02;

}
