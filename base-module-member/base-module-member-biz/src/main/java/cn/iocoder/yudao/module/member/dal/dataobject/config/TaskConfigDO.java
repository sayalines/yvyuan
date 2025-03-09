package cn.iocoder.yudao.module.member.dal.dataobject.config;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 任务配置 DO
 *
 * @author 超级管理员
 */
@TableName("task_config")
@KeySequence("task_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskConfigDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 图标
     */
    private String logo;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 奖励积分
     */
    private Integer point;
    /**
     * 奖励彩虹值
     */
    private Integer rainbow;
    /**
     * 限制类型
     */
    private Integer limitType;
    /**
     * 限制次数
     */
    private Integer limitCount;
    /**
     * 备注
     */
    private String remarks;

}