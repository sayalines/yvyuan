package cn.iocoder.yudao.module.member.dal.dataobject.exchangeRecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员兑换记录 DO
 *
 * @author 超级管理员
 */
@TableName("member_exchange_record")
@KeySequence("member_exchange_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRecordDO extends BaseDO {

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
     * 配置ID
     */
    private Long configId;
    /**
     * 配置明细ID
     */
    private Long configItemId;
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
     * 是否使用
     */
    private Boolean isUse;
    /**
     * 使用时间
     */
    private LocalDateTime useTime;
    /**
     * 激活时间
     */
    private LocalDateTime activeTime;
    /**
     * 有效开始时间
     */
    private LocalDateTime validStartTime;
    /**
     * 有效结束时间
     */
    private LocalDateTime validEndTime;

    /**
     * 结果ID
     */
    private Long resultId;

}