package cn.iocoder.yudao.module.member.dal.dataobject.articlevisit;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 文章访问日志 DO
 *
 * @author 超级管理员
 */
@TableName("member_article_visit")
@KeySequence("member_article_visit_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVisitDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章分类ID
     */
    private Long articleCategoryId;
    /**
     * 文章分类
     */
    private String articleCategory;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户手机号
     */
    private String userMobile;

}