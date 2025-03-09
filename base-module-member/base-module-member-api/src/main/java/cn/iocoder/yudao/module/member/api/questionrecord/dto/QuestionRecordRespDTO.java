package cn.iocoder.yudao.module.member.api.questionrecord.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class QuestionRecordRespDTO {
    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 问卷ID
     */
    private Long questionId;

    /**
     * 量表主题
     */
    private String questionName;

    /**
     * 答案内容
     */
    private String answer;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建时间(格式化)
     */
    private String createTimeFormat;
    /**
     * 答题开始时间
     */
    private LocalDateTime startTime;
    /**
     * 答题开始时间(格式化)
     */
    private String startTimeFormat;

    /**
     * 答题结束时间
     */
    private LocalDateTime endTime;
    /**
     * 答题结束时间(格式化)
     */
    private String endTimeFormat;
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
