package cn.iocoder.yudao.module.member.api.questionrecord.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 问卷记录
 */
@Data
public class QuestionRecordDTO {
    /**
     * ID
     */
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
