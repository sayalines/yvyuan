package cn.iocoder.yudao.module.system.service.questiondimension;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.questiondimension.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.questiondimension.QuestionDimensionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.module.system.dal.dataobject.questionfactor.QuestionFactorDO;

/**
 * 量表维度 Service 接口
 *
 * @author 超级管理员
 */
public interface QuestionDimensionService {

    /**
     * 创建量表维度
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQuestionDimension(@Valid QuestionDimensionSaveReqVO createReqVO);

    /**
     * 更新量表维度
     *
     * @param updateReqVO 更新信息
     */
    void updateQuestionDimension(@Valid QuestionDimensionSaveReqVO updateReqVO);

    /**
     * 删除量表维度
     *
     * @param id 编号
     */
    void deleteQuestionDimension(Long id);

    /**
     * 获得量表维度
     *
     * @param id 编号
     * @return 量表维度
     */
    QuestionDimensionDO getQuestionDimension(Long id);

    /**
     * 获得量表维度分页
     *
     * @param pageReqVO 分页查询
     * @return 量表维度分页
     */
    PageResult<QuestionDimensionDO> getQuestionDimensionPage(QuestionDimensionPageReqVO pageReqVO);
    /**
     * 获得量表维度列表
     * @param questionId
     * @return
     */
    List<QuestionDimensionDO> findList(Long questionId);
}