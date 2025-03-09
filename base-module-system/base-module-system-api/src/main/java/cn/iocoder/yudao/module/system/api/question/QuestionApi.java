package cn.iocoder.yudao.module.system.api.question;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.question.dto.QuestionDTO;
import cn.iocoder.yudao.module.system.api.question.dto.QuestionPageReqDTO;

import java.util.Collection;
import java.util.List;

/**
 * 量表管理
 */
public interface QuestionApi {
    /**
     * 获得量表管理列表
     *
     * @return 量表管理列表
     */
    List<QuestionDTO> getQuestionList(Collection<Long> ids);

    /**
     * 获得量表管理
     * @param id
     * @return
     */
    QuestionDTO getQuestion(Long id);
    /**
     * 获得量表管理分页
     *
     * @param pageReqVO 分页查询
     * @return 量表管理分页
     */
    PageResult<QuestionDTO> getQuestionPage(QuestionPageReqDTO pageReqVO);
}
