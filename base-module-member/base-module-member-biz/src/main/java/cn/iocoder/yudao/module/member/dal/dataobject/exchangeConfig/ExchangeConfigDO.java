package cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfig;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 兑换配置 DO
 *
 * @author 超级管理员
 */
@TableName("exchange_config")
@KeySequence("exchange_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeConfigDO extends BaseDO {

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
     * 业务类型
     */
    private Integer bizType;
    /**
     * 使用次数
     */
    private Integer useCount;
    /**
     * 编号前缀
     */
    private String prefix;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 物品ID
     */
    private Long bizId;
    /**
     * 有效开始时间
     */
    private LocalDateTime validStartTime;
    /**
     * 有效结束时间
     */
    private LocalDateTime validEndTime;
    /**
     * 兑换码长度
     */
    private Integer codeLength;
    /**
     * 兑换上限
     */
    private Integer dayCount;
    /**
     * 黑名单用户
     */
    private String blackUsers;
    /**
     * 白名单用户
     */
    private String whiteUsers;
}