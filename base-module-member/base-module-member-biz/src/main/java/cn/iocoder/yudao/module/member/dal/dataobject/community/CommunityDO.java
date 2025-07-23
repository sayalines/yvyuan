package cn.iocoder.yudao.module.member.dal.dataobject.community;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 论坛文章 DO
 *
 * @author 超级管理员
 */
@TableName("ext_community")
@KeySequence("ext_community_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 品牌ID
     */
    private Long brandId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 封面图
     */
    private String picUrl;
    /**
     * 附件
     */
    private String fileUrl;
    /**
     * 是否热门
     *
     * 枚举 {@link TODO system_yes_no 对应的类}
     */
    private Integer isHot;
    /**
     * 是否精选
     *
     * 枚举 {@link TODO system_yes_no 对应的类}
     */
    private Integer isChoice;
    /**
     * 收藏数
     */
    private Long collectCount;
    /**
     * 点赞数
     */
    private Long likeCount;
    /**
     * 评论数
     */
    private Long reviewCount;
    /**
     * 状态
     *
     * 枚举 {@link TODO ext_community_status 对应的类}
     */
    private Integer status;
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    /**
     * 审核人ID
     */
    private Long auditorId;
    /**
     * 拒绝原因
     */
    private String reason;

}