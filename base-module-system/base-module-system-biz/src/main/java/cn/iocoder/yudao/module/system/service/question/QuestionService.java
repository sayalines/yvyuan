package cn.iocoder.yudao.module.system.service.question;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.question.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.question.QuestionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 量表管理 Service 接口
 *
 * @author 超级管理员
 */
public interface QuestionService {

    /**
     * 创建量表管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQuestion(@Valid QuestionSaveReqVO createReqVO);

    /**
     * 更新量表管理
     *
     * @param updateReqVO 更新信息
     */
    void updateQuestion(@Valid QuestionSaveReqVO updateReqVO);

    /**
     * 删除量表管理
     *
     * @param id 编号
     */
    void deleteQuestion(Long id);

    /**
     * 获得量表管理
     *
     * @param id 编号
     * @return 量表管理
     */
    QuestionDO getQuestion(Long id);

    /**
     * 获得量表管理分页
     *
     * @param pageReqVO 分页查询
     * @return 量表管理分页
     */
    PageResult<QuestionDO> getQuestionPage(QuestionPageReqVO pageReqVO);

    /**
     * 获得量表管理列表
     *
     * @return 量表管理列表
     */
    List<QuestionDO> getQuestionList(Collection<Long> ids);

}