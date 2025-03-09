package cn.iocoder.yudao.module.system.dal.dataobject.questiontopic;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 量表题目 DO
 *
 * @author 超级管理员
 */
@TableName("system_question_topic")
@KeySequence("system_question_topic_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTopicDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 问卷ID
     */
    private Long questionId;
    /**
     * 题目编号
     */
    private String code;
    /**
     * 题目名称
     */
    private String name;
    /**
     * 题目类型
     */
    private Integer type;
    /**
     * 选项内容
     */
    private String optContent;
    /**
     * 排序号
     */
    private Integer orderNo;

}