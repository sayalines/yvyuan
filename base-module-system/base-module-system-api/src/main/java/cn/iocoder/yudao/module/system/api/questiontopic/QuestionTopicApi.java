package cn.iocoder.yudao.module.system.api.questiontopic;

import cn.iocoder.yudao.module.system.api.questiontopic.dto.QuestionTopicDTO;

import java.util.Collection;
import java.util.List;

/**
 * 题目
 */
public interface QuestionTopicApi {
    /**
     * 获取题目
     * @param questionId
     * @param currentNo
     * @param orderBy
     * @return
     */
    QuestionTopicDTO getQuestionTopic(Long questionId,String topicCode,Integer currentNo,String orderBy);

    /**
     * 获取题目总数
     * @param questionId
     * @return
     */
    Long getTotalCount(Long questionId);
    /**
     * 获取题目列表
     * @param ids
     * @return
     */
    List<QuestionTopicDTO> getQuestionTopicList(Collection<Long> ids);
    /**
     * 获取题目列表
     * @param questionId
     * @return
     */
    List<QuestionTopicDTO> getQuestionTopicListById(Long questionId);
}
