package cn.iocoder.yudao.module.member.api.questionrecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.api.questionrecord.dto.QuestionRecordDTO;
import cn.iocoder.yudao.module.member.api.questionrecord.dto.QuestionRecordPageDTO;
import cn.iocoder.yudao.module.member.api.questionrecord.dto.QuestionRecordRespDTO;
import cn.iocoder.yudao.module.member.controller.admin.questionrecord.vo.QuestionRecordPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.questionrecord.vo.QuestionRecordSaveReqVO;
import cn.iocoder.yudao.module.member.service.questionrecord.QuestionRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 问卷记录
 */
@Service
public class QuestionRecordApiImpl implements QuestionRecordApi {
    @Resource
    private QuestionRecordService questionRecordService;

    @Override
    public Long saveQuestionRecord(QuestionRecordDTO entity) {
        QuestionRecordSaveReqVO createReqVO = BeanUtils.toBean(entity,QuestionRecordSaveReqVO.class);
        return questionRecordService.createQuestionRecord(createReqVO);
    }

    @Override
    public void deleteQuestionRecord(Long id) {
        questionRecordService.deleteQuestionRecord(id);
    }

    @Override
    public QuestionRecordRespDTO getQuestionRecord(Long id) {
        return BeanUtils.toBean(questionRecordService.getQuestionRecord(id),QuestionRecordRespDTO.class);
    }

    @Override
    public PageResult<QuestionRecordRespDTO> getQuestionRecordPage(QuestionRecordPageDTO pageReqVO) {
        return BeanUtils.toBean(questionRecordService.getQuestionRecordPage(BeanUtils.toBean(pageReqVO, QuestionRecordPageReqVO.class)),QuestionRecordRespDTO.class);
    }
}
