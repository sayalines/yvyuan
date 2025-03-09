package cn.iocoder.yudao.server.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * 文章管理
 */
@Data
public class ApiArticleDO {

    /**
     * 文章管理编号
     */
    @TableId
    private Long id;
    /**
     * 分类编号 ArticleCategoryDO#id
     */
    @Schema(description = "分类编号")
    private Long categoryId;
    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String categoryName;
    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    private String title;
    /**
     * 文章作者
     */
    @Schema(description = "作者")
    private String author;
    /**
     * 文章封面图片地址
     */
    @Schema(description = "文章封面图片地址")
    private String picUrl;
    /**
     * 文章简介
     */
    @Schema(description = "内容简介")
    private String introduction;
    /**
     * 浏览次数
     */
    @Schema(description = "浏览次数")
    private Integer browseCount;
    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    @Schema(description = "状态")
    private Integer status;
//    /**
//     * 是否热门(小程序)
//     */
//    @Schema(description = "是否热门(小程序)")
//    private Boolean recommendHot;
//    /**
//     * 是否轮播图(小程序)
//     */
//    @Schema(description = "是否轮播图(小程序)")
//    private Boolean recommendBanner;
    /**
     * 文章内容
     */
    @Schema(description = "文章内容")
    private String content;
    /**
     * 发布时间
     */
    @Schema(description = "发布时间")
    private String publishDate;

//    @Schema(description = "标签")
//    private String tag;

    @Schema(description = "外链接地址")
    private String href;

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

//    @Schema(description = "备用属性03")
//    private String attr03;
//
//    @Schema(description = "备用属性04")
//    private String attr04;
//
//    @Schema(description = "备用属性05")
//    private String attr05;
    @Schema(description = "上一篇文章")
    private ApiArticleDO previousArticle;

    @Schema(description = "下一篇文章")
    private ApiArticleDO nextArticle;

    @Schema(description = "点击数")
    private Long hitCount;
}
