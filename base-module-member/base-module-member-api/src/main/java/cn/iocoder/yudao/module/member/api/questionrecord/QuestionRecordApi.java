package cn.iocoder.yudao.module.member.api.questionrecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.api.questionrecord.dto.QuestionRecordDTO;
import cn.iocoder.yudao.module.member.api.questionrecord.dto.QuestionRecordPageDTO;
import cn.iocoder.yudao.module.member.api.questionrecord.dto.QuestionRecordRespDTO;

/**
 * 问卷记录
 */
public interface QuestionRecordApi {
    /**
     * 新增问卷记录
     * @param entity
     * @return
     */
    Long saveQuestionRecord(QuestionRecordDTO entity);
    /**
     * 删除问卷记录
     *
     * @param id 编号
     */
    void deleteQuestionRecord(Long id);

    /**
     * 获得问卷记录
     *
     * @param id 编号
     * @return 问卷记录
     */
    QuestionRecordRespDTO getQuestionRecord(Long id);

    /**
     * 获得问卷记录分页
     *
     * @param pageReqVO 分页查询
     * @return 问卷记录分页
     */
    PageResult<QuestionRecordRespDTO> getQuestionRecordPage(QuestionRecordPageDTO pageReqVO);
}
