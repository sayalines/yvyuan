package cn.iocoder.yudao.module.member.dal.dataobject.membervisitlog;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 访客信息日志 DO
 *
 * @author 超级管理员
 */
@TableName("member_visit_log")
@KeySequence("member_visit_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVisitLogDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 统计时间
     */
    private LocalDateTime statTime;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户IP
     */
    private String userIp;
    /**
     * 手机型号
     */
    private String mobileModel;
    /**
     * 第一次访问时间
     */
    private LocalDateTime minTime;
    /**
     * 最后一次访问时间
     */
    private LocalDateTime maxTime;

}