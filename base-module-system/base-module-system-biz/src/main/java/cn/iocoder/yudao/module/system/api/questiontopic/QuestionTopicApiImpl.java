package cn.iocoder.yudao.module.system.api.questiontopic;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.questiontopic.dto.QuestionTopicDTO;
import cn.iocoder.yudao.module.system.service.questiontopic.QuestionTopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 *题目
 */
@Service
public class QuestionTopicApiImpl implements QuestionTopicApi{

    @Resource
    private QuestionTopicService questionTopicService;

    @Override
    public QuestionTopicDTO getQuestionTopic(Long questionId, String topicCode, Integer currentNo, String orderBy) {
        return BeanUtils.toBean(questionTopicService.getQuestionTopic(questionId,topicCode,currentNo,orderBy),QuestionTopicDTO.class);
    }

    @Override
    public Long getTotalCount(Long questionId) {
        return questionTopicService.getTotalCount(questionId);
    }

    @Override
    public List<QuestionTopicDTO> getQuestionTopicList(Collection<Long> ids) {
        return BeanUtils.toBean(questionTopicService.getQuestionTopicList(ids),QuestionTopicDTO.class);
    }

    @Override
    public List<QuestionTopicDTO> getQuestionTopicListById(Long questionId) {
        return BeanUtils.toBean(questionTopicService.getQuestionTopicListById(questionId),QuestionTopicDTO.class);
    }
}
