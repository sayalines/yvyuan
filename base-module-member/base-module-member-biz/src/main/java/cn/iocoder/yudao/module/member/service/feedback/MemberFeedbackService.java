package cn.iocoder.yudao.module.member.service.feedback;

import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.feedback.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.feedback.MemberFeedbackDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 问题反馈 Service 接口
 *
 * @author 超级管理员
 */
public interface MemberFeedbackService {

    /**
     * 创建问题反馈
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQuestion(@Valid MemberFeedbackSaveReqVO createReqVO);

    /**
     * 更新问题反馈
     *
     * @param updateReqVO 更新信息
     */
    void updateQuestion(@Valid MemberFeedbackSaveReqVO updateReqVO);

    /**
     * 删除问题反馈
     *
     * @param id 编号
     */
    void deleteQuestion(Long id);

    /**
     * 获得问题反馈
     *
     * @param id 编号
     * @return 问题反馈
     */
    MemberFeedbackDO getQuestion(Long id);

    /**
     * 获得问题反馈分页
     *
     * @param pageReqVO 分页查询
     * @return 问题反馈分页
     */
    PageResult<MemberFeedbackDO> getQuestionPage(MemberFeedbackPageReqVO pageReqVO);

    /**
     * 问题反馈解决状态改变
     *
     * @param id      问题编号
     * @param result 审核结果
     * @return 操作结果状态码
     */

    void auditQuestion(Long id, String result);
}