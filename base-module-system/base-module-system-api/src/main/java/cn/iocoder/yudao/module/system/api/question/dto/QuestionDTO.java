package cn.iocoder.yudao.module.system.api.question.dto;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 量表管理 DO
 *
 * @author 超级管理员
 */
@Data
public class QuestionDTO {
    /**
     * ID
     */
    private Long id;
    /**
     * 量表主题
     */
    private String name;
    /**
     * 封面图
     */
    private String picUrl;
    /**
     * 简介
     */
    private String description;
    /**
     * 内容
     */
    private String content;
    /**
     * 默认评分
     */
    private BigDecimal score;
    /**
     * 题目数
     */
    private Integer topicCount;
    /**
     * 限制时间
     */
    private Integer limitTime;
    /**
     * 使用人数
     */
    private Integer memberNum;
    /**
     * 热度
     */
    private String hitCount;
    /**
     * 指导语
     */
    private String comment;
    /**
     * 是否允许查看结果
     */
    private Integer isAnswer;
    /**
     * 是否允许查看指导建议
     */
    private Integer isComment;
    /**
     * 是否需要完善会员资料
     */
    private Integer isMemberinfo;
    /**
     * 是否需要图形报表
     */
    private Integer isGraphic;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 开始时间(格式化)
     */
    private String startTimeFormat;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 结束时间(格式化)
     */
    private String endTimeFormat;
    /**
     * 重测时间
     */
    private String resurveyTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建时间(格式化)
     */
    private String createTimeFormat;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 因子Y轴最大值
     */
    private Integer factorMaxAxis;
    /**
     * 维度Y轴最大值
     */
    private Integer dimensionMaxAxis;
    /**
     * 总分Y轴最大值
     */
    private Integer totalMaxAxis;
    /**
     * 显示名称
     */
    private String displayName;
    /**
     * 排序号
     */
    private Integer orderNo;
    /**
     * 是否统计
     */
    private Integer isStat;
}
