package cn.iocoder.yudao.module.member.dal.dataobject.productvisitlog;

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
 * 商品访问日志 DO
 *
 * @author 超级管理员
 */
@TableName("product_visit_log")
@KeySequence("product_visit_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVisitLogDO extends BaseDO {

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
     * 商品ID
     */
    private Long spuId;
    /**
     * 商品名称
     */
    private String spuName;
    /**
     * 点击量
     */
    private Integer hitCount;
    /**
     * 加购件数
     */
    private Integer cartCount;
    /**
     * 第一次访问时间
     */
    private LocalDateTime minTime;
    /**
     * 最后一次访问时间
     */
    private LocalDateTime maxTime;

}