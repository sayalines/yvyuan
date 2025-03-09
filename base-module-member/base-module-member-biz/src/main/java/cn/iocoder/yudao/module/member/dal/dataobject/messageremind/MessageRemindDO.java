package cn.iocoder.yudao.module.member.dal.dataobject.messageremind;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员消息提醒 DO
 *
 * @author 超级管理员
 */
@TableName("member_message_remind")
@KeySequence("member_message_remind_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRemindDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * openID
     */
    private String openid;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 业务ID
     */
    private Long bizId;
    /**
     * 是否已提醒
     */
    private Boolean isRemind;
    /**
     * 提醒时间
     */
    private LocalDateTime remindTime;

}