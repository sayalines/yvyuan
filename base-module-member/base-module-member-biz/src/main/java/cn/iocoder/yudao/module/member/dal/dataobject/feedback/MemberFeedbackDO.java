package cn.iocoder.yudao.module.member.dal.dataobject.feedback;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 问题反馈 DO
 *
 * @author 超级管理员
 */
@TableName("member_feedback")
@KeySequence("member_feedback_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFeedbackDO extends BaseDO {

    /**
     * 问题编号
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 问题内容
     */
    private String content;
    /**
     * 解决状态
     */
    private Integer ifSolve;

}