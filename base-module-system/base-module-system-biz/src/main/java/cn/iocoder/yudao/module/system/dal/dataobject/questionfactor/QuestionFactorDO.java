package cn.iocoder.yudao.module.system.dal.dataobject.questionfactor;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 量表因子 DO
 *
 * @author 超级管理员
 */
@TableName(value = "system_question_factor", autoResultMap = true)
@KeySequence("system_question_factor_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionFactorDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 量表ID
     */
    private Long questionId;
    /**
     * 主题
     */
    private String name;
    /**
     * 简介
     */
    private String description;
    /**
     * 推荐
     */
    private String suggest;
    /**
     * 选择题目
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> topics;
    /**
     * 分值计算类型
     */
    private Integer scoreType;
    /**
     * 常量值
     */
    private BigDecimal constantValue;
    /**
     * 常模值
     */
    private BigDecimal constantScore;
    /**
     * 常模类型
     */
    private Integer constantScoreType;
    /**
     * 选项内容
     */
    private String optContent;
    /**
     * 关键字内容
     */
    private String keywordContent;
    /**
     * 排序号
     */
    private Integer orderNo;
    /**
     * 是否为总分汇总
     */
    private Integer isTotal;

    /**
     * 是否需要显示
     */
    private Integer isShow;

}