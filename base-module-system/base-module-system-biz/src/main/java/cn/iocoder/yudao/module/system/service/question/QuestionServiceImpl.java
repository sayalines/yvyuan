package cn.iocoder.yudao.module.system.service.question;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.system.controller.admin.question.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.question.QuestionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.system.dal.mysql.question.QuestionMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 量表管理 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class QuestionServiceImpl implements QuestionService {

    @Resource
    private QuestionMapper questionMapper;

    @Override
    public Long createQuestion(QuestionSaveReqVO createReqVO) {
        // 插入
        QuestionDO question = BeanUtils.toBean(createReqVO, QuestionDO.class);
        questionMapper.insert(question);
        // 返回
        return question.getId();
    }

    @Override
    public void updateQuestion(QuestionSaveReqVO updateReqVO) {
        // 校验存在
        validateQuestionExists(updateReqVO.getId());
        // 更新
        QuestionDO updateObj = BeanUtils.toBean(updateReqVO, QuestionDO.class);
        questionMapper.updateById(updateObj);
    }

    @Override
    public void deleteQuestion(Long id) {
        // 校验存在
        validateQuestionExists(id);
        // 删除
        questionMapper.deleteById(id);
    }

    private void validateQuestionExists(Long id) {
        if (questionMapper.selectById(id) == null) {
            throw exception(QUESTION_NOT_EXISTS);
        }
    }

    @Override
    public QuestionDO getQuestion(Long id) {
        return questionMapper.selectById(id);
    }

    @Override
    public PageResult<QuestionDO> getQuestionPage(QuestionPageReqVO pageReqVO) {
        return questionMapper.selectPage(pageReqVO);
    }

    @Override
    public List<QuestionDO> getQuestionList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return questionMapper.selectBatchIds(ids);
    }

}