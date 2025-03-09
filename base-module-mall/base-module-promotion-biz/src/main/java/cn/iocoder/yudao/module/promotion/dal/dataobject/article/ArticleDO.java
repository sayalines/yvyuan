package cn.iocoder.yudao.module.promotion.dal.dataobject.article;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章管理 DO
 *
 * @author HUIHUI
 */
@TableName(value = "promotion_article", autoResultMap = true)
@KeySequence("promotion_article_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDO extends BaseDO {

    /**
     * 文章管理编号
     */
    @TableId
    private Long id;
    /**
     * 分类编号 ArticleCategoryDO#id
     */
    private Long categoryId;
    /**
     * 部门id  公会id
     */
    private Long deptId;
//    /**
//     * 关联商品编号 ProductSpuDO#id
//     */
//    private Long spuId;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章作者
     */
    private String author;
    /**
     * 文章封面图片地址
     */
    private String picUrl;
    /**
     * 文章简介
     */
    private String introduction;
    /**
     * 浏览次数
     */
    private Integer browseCount;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 是否列表展示
     */
    private Integer ifExhibition;
//    /**
//     * 是否热门(小程序)
//     */
//    private Boolean recommendHot;
//    /**
//     * 是否轮播图(小程序)
//     */
//    private Boolean recommendBanner;
    /**
     * 文章内容
     */
    private String content;

    /**
     * 发布时间
     */
    private LocalDateTime publishDate;
    /**
     * 标签
     */
    private String tag;
    /**
     * 外链接地址
     */
    private String href;
    /**
     * 多图
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> pictures;
    /**
     * 小程序码图片地址
     */
    private String qrcodeUrl;
    /**
     * 附件
     */
    private String files;
    /**
     * 备注
     */
    private String remark;
    /**
     * 备用属性01
     */
    private String attr01;
    /**
     * 备用属性02
     */
    private String attr02;
    /**
     * 备用属性03
     */
    private String attr03;
    /**
     * 备用属性04
     */
    private String attr04;
    /**
     * 备用属性05
     */
    private String attr05;

    /**
     * 点击数
     */
    private Long hitCount;


}
