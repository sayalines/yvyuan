package cn.iocoder.yudao.module.system.service.questiondimension;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.util.*;
import cn.iocoder.yudao.module.system.controller.admin.questiondimension.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.questiondimension.QuestionDimensionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.dal.mysql.questiondimension.QuestionDimensionMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 量表维度 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class QuestionDimensionServiceImpl implements QuestionDimensionService {

    @Resource
    private QuestionDimensionMapper questionDimensionMapper;

    @Override
    public Long createQuestionDimension(QuestionDimensionSaveReqVO createReqVO) {
        // 插入
        QuestionDimensionDO questionDimension = BeanUtils.toBean(createReqVO, QuestionDimensionDO.class);
        questionDimensionMapper.insert(questionDimension);
        // 返回
        return questionDimension.getId();
    }

    @Override
    public void updateQuestionDimension(QuestionDimensionSaveReqVO updateReqVO) {
        // 校验存在
        validateQuestionDimensionExists(updateReqVO.getId());
        // 更新
        QuestionDimensionDO updateObj = BeanUtils.toBean(updateReqVO, QuestionDimensionDO.class);
        questionDimensionMapper.updateById(updateObj);
    }

    @Override
    public void deleteQuestionDimension(Long id) {
        // 校验存在
        validateQuestionDimensionExists(id);
        // 删除
        questionDimensionMapper.deleteById(id);
    }

    private void validateQuestionDimensionExists(Long id) {
        if (questionDimensionMapper.selectById(id) == null) {
            throw exception(QUESTION_DIMENSION_NOT_EXISTS);
        }
    }

    @Override
    public QuestionDimensionDO getQuestionDimension(Long id) {
        return questionDimensionMapper.selectById(id);
    }

    @Override
    public PageResult<QuestionDimensionDO> getQuestionDimensionPage(QuestionDimensionPageReqVO pageReqVO) {
        return questionDimensionMapper.selectPage(pageReqVO);
    }

    @Override
    public List<QuestionDimensionDO> findList(Long questionId) {
        LambdaQueryWrapper<QuestionDimensionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(QuestionDimensionDO::getQuestionId,questionId)
                .orderByAsc(QuestionDimensionDO::getOrderNo,QuestionDimensionDO::getId);
        return questionDimensionMapper.selectList(wrapper);
    }

}