package cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfigItem;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 兑换配置明细 DO
 *
 * @author 超级管理员
 */
@TableName("exchange_config_item")
@KeySequence("exchange_config_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeConfigItemDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 配置ID
     */
    private Long configId;
    /**
     * 编码
     */
    private String code;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 物品ID
     */
    private Long bizId;
    /**
     * 总次数
     */
    private Integer totalCount;
    /**
     * 已使用次数
     */
    private Integer useCount;
    /**
     * 有效开始时间
     */
    private LocalDateTime validStartTime;
    /**
     * 有效结束时间
     */
    private LocalDateTime validEndTime;

}