package cn.iocoder.yudao.module.system.api.question;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.question.dto.QuestionDTO;
import cn.iocoder.yudao.module.system.api.question.dto.QuestionPageReqDTO;
import cn.iocoder.yudao.module.system.controller.admin.question.vo.QuestionPageReqVO;
import cn.iocoder.yudao.module.system.service.question.QuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 量表管理
 */
@Service
public class QuestionApiImpl implements QuestionApi{
    @Resource
    private QuestionService questionService;

    @Override
    public List<QuestionDTO> getQuestionList(Collection<Long> ids) {
        return BeanUtils.toBean(questionService.getQuestionList(ids),QuestionDTO.class);
    }

    @Override
    public QuestionDTO getQuestion(Long id) {
        return BeanUtils.toBean(questionService.getQuestion(id),QuestionDTO.class);
    }

    @Override
    public PageResult<QuestionDTO> getQuestionPage(QuestionPageReqDTO pageReqVO) {
        return BeanUtils.toBean(questionService.getQuestionPage(BeanUtils.toBean(pageReqVO, QuestionPageReqVO.class)),QuestionDTO.class);
    }
}
