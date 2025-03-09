package cn.iocoder.yudao.module.member.dal.dataobject.questionrecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 问卷记录 DO
 *
 * @author 超级管理员
 */
@TableName("member_question_record")
@KeySequence("member_question_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRecordDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 问卷ID
     */
    private Long questionId;
    /**
     * 答案内容
     */
    private String answer;
    /**
     * 答题开始时间
     */
    private LocalDateTime startTime;
    /**
     * 答题结束时间
     */
    private LocalDateTime endTime;
    /**
     * 答题时间
     */
    private String answerTime;
    /**
     * 报告标题
     */
    private String resultTitle;
    /**
     * 报告结果
     */
    private String result;
    /**
     * 报告参数
     */
    private String resultParam;
    /**
     * 统计结果
     */
    private String resultStatus;

}