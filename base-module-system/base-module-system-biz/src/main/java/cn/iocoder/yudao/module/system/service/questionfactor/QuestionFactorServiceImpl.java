package cn.iocoder.yudao.module.system.service.questionfactor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.util.*;
import cn.iocoder.yudao.module.system.controller.admin.questionfactor.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.questionfactor.QuestionFactorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.dal.mysql.questionfactor.QuestionFactorMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 量表因子 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class QuestionFactorServiceImpl implements QuestionFactorService {

    @Resource
    private QuestionFactorMapper questionFactorMapper;

    @Override
    public Long createQuestionFactor(QuestionFactorSaveReqVO createReqVO) {
        // 插入
        QuestionFactorDO questionFactor = BeanUtils.toBean(createReqVO, QuestionFactorDO.class);
        questionFactorMapper.insert(questionFactor);
        // 返回
        return questionFactor.getId();
    }

    @Override
    public void updateQuestionFactor(QuestionFactorSaveReqVO updateReqVO) {
        // 校验存在
        validateQuestionFactorExists(updateReqVO.getId());
        // 更新
        QuestionFactorDO updateObj = BeanUtils.toBean(updateReqVO, QuestionFactorDO.class);
        questionFactorMapper.updateById(updateObj);
    }

    @Override
    public void deleteQuestionFactor(Long id) {
        // 校验存在
        validateQuestionFactorExists(id);
        // 删除
        questionFactorMapper.deleteById(id);
    }

    private void validateQuestionFactorExists(Long id) {
        if (questionFactorMapper.selectById(id) == null) {
            throw exception(QUESTION_FACTOR_NOT_EXISTS);
        }
    }

    @Override
    public QuestionFactorDO getQuestionFactor(Long id) {
        return questionFactorMapper.selectById(id);
    }

    @Override
    public PageResult<QuestionFactorDO> getQuestionFactorPage(QuestionFactorPageReqVO pageReqVO) {
        return questionFactorMapper.selectPage(pageReqVO);
    }

    @Override
    public List<QuestionFactorDO> findList(Long questionId) {
        LambdaQueryWrapper<QuestionFactorDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(QuestionFactorDO::getQuestionId,questionId)
                .orderByAsc(QuestionFactorDO::getOrderNo,QuestionFactorDO::getId);
        return questionFactorMapper.selectList(wrapper);
    }

}