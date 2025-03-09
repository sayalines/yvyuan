package cn.iocoder.yudao.module.promotion.dal.dataobject.article;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 文章分类 DO
 *
 * @author 超级管理员
 */
@TableName("promotion_article_category")
@KeySequence("promotion_article_category_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategoryDO extends BaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 上级分类
     */
    private Long parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 图片地址
     */
    private String picUrl;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
}