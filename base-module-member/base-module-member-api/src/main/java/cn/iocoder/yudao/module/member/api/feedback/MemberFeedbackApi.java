package cn.iocoder.yudao.module.member.api.feedback;

import cn.iocoder.yudao.module.member.api.feedback.dto.MemberFeedbackDto;


public interface MemberFeedbackApi {
    /**
     * 添加问题反馈
     *
     * @param memberFeedbackDto
     */
    Integer add(MemberFeedbackDto memberFeedbackDto);
}
