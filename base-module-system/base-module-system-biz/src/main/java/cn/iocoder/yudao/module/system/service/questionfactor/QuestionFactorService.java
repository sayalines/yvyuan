package cn.iocoder.yudao.module.system.service.questionfactor;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.questionfactor.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.questionfactor.QuestionFactorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 量表因子 Service 接口
 *
 * @author 超级管理员
 */
public interface QuestionFactorService {

    /**
     * 创建量表因子
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQuestionFactor(@Valid QuestionFactorSaveReqVO createReqVO);

    /**
     * 更新量表因子
     *
     * @param updateReqVO 更新信息
     */
    void updateQuestionFactor(@Valid QuestionFactorSaveReqVO updateReqVO);

    /**
     * 删除量表因子
     *
     * @param id 编号
     */
    void deleteQuestionFactor(Long id);

    /**
     * 获得量表因子
     *
     * @param id 编号
     * @return 量表因子
     */
    QuestionFactorDO getQuestionFactor(Long id);

    /**
     * 获得量表因子分页
     *
     * @param pageReqVO 分页查询
     * @return 量表因子分页
     */
    PageResult<QuestionFactorDO> getQuestionFactorPage(QuestionFactorPageReqVO pageReqVO);
    /**
     * 获得量表因子列表
     * @param questionId
     * @return
     */
    List<QuestionFactorDO> findList(Long questionId);
}