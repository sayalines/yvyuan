package cn.iocoder.yudao.module.system.api.questiontopic.dto;

import lombok.Data;

/**
 * 量表题目 DO
 *
 * @author 超级管理员
 */
@Data
public class QuestionTopicDTO {
    /**
     * ID
     */
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
