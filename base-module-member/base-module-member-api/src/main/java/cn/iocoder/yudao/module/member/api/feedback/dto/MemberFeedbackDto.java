package cn.iocoder.yudao.module.member.api.feedback.dto;

import lombok.Data;

@Data
public class MemberFeedbackDto {
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 内容
     */
    private String content;
}
