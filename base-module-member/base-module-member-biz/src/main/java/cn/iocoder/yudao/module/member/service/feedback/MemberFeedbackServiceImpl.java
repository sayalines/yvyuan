package cn.iocoder.yudao.module.member.service.feedback;

import cn.iocoder.yudao.module.member.enums.MemberQuestionStatus;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import cn.iocoder.yudao.module.member.controller.admin.feedback.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.feedback.MemberFeedbackDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.feedback.MemberFeedbackMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 问题反馈 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class MemberFeedbackServiceImpl implements MemberFeedbackService {

    @Resource
    private MemberFeedbackMapper memberFeedbackMapper;

    @Override
    public Long createQuestion(MemberFeedbackSaveReqVO createReqVO) {
        // 插入
        MemberFeedbackDO question = BeanUtils.toBean(createReqVO, MemberFeedbackDO.class);
        memberFeedbackMapper.insert(question);
        // 返回
        return question.getId();
    }

    @Override
    public void updateQuestion(MemberFeedbackSaveReqVO updateReqVO) {
        // 校验存在
        validateQuestionExists(updateReqVO.getId());
        // 更新
        MemberFeedbackDO updateObj = BeanUtils.toBean(updateReqVO, MemberFeedbackDO.class);
        memberFeedbackMapper.updateById(updateObj);
    }

    @Override
    public void deleteQuestion(Long id) {
        // 校验存在
        validateQuestionExists(id);
        // 删除
        memberFeedbackMapper.deleteById(id);
    }

    public MemberFeedbackDO validateQuestionExists(Long id) {
        MemberFeedbackDO memberQuestion = memberFeedbackMapper.selectById(id);
        if (memberQuestion == null) {
            throw new RuntimeException("当前问题不存在");
        }
        return memberQuestion;
    }

    @Override
    public MemberFeedbackDO getQuestion(Long id) {
        return memberFeedbackMapper.selectById(id);
    }

    @Override
    public PageResult<MemberFeedbackDO> getQuestionPage(MemberFeedbackPageReqVO pageReqVO) {
        return memberFeedbackMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditQuestion(Long id, String result) {
        try {
            MemberFeedbackDO memberQuestion = validateQuestionExists(id);
            int newState = "已解决".equalsIgnoreCase(result) ? MemberQuestionStatus.AUDIT_SUCCESS.getCode() : MemberQuestionStatus.AUDIT_FAIL.getCode();
            memberQuestion.setIfSolve(newState);
            memberFeedbackMapper.updateById(memberQuestion);
        } catch (Exception e) {
            // 处理异常
        }
    }

}