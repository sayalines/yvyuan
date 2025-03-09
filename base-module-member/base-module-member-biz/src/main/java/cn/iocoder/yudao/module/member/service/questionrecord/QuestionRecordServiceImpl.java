package cn.iocoder.yudao.module.member.service.questionrecord;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.questionrecord.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.questionrecord.QuestionRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.questionrecord.QuestionRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 问卷记录 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class QuestionRecordServiceImpl implements QuestionRecordService {

    @Resource
    private QuestionRecordMapper questionRecordMapper;
    @Resource
    private MemberUserService memberUserService;

    @Override
    public Long createQuestionRecord(QuestionRecordSaveReqVO createReqVO) {
        // 插入
        QuestionRecordDO questionRecord = BeanUtils.toBean(createReqVO, QuestionRecordDO.class);
        questionRecordMapper.insert(questionRecord);
        // 返回
        return questionRecord.getId();
    }

    @Override
    public void updateQuestionRecord(QuestionRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateQuestionRecordExists(updateReqVO.getId());
        // 更新
        QuestionRecordDO updateObj = BeanUtils.toBean(updateReqVO, QuestionRecordDO.class);
        questionRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteQuestionRecord(Long id) {
        // 校验存在
        validateQuestionRecordExists(id);
        // 删除
        questionRecordMapper.deleteById(id);
    }

    private void validateQuestionRecordExists(Long id) {
        if (questionRecordMapper.selectById(id) == null) {
            throw exception(QUESTION_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public QuestionRecordDO getQuestionRecord(Long id) {
        return questionRecordMapper.selectById(id);
    }

    @Override
    public PageResult<QuestionRecordDO> getQuestionRecordPage(QuestionRecordPageReqVO pageReqVO) {
        // 根据用户昵称查询出用户ids
        Set<Long> userIds = null;
        if (StringUtils.isNotBlank(pageReqVO.getMobile())) {
            MemberUserDO userDO = memberUserService.getUserByMobile(pageReqVO.getMobile());
            if (userDO!=null){
                userIds = new HashSet<>();
                userIds.add(userDO.getId());
            }else{
                return PageResult.empty();
            }
        }else if (StringUtils.isNotBlank(pageReqVO.getNickname())){
            List<MemberUserDO> users = memberUserService.getUserListByNickname(pageReqVO.getNickname());
            // 如果查询用户结果为空直接返回无需继续查询
            if (CollUtil.isEmpty(users)) {
                return PageResult.empty();
            }
            userIds = convertSet(users, MemberUserDO::getId);
        }
        return questionRecordMapper.selectPage(pageReqVO,userIds);
    }

    @Override
    public List<Map<String, Object>> findListByTime(LocalDateTime beginTime, LocalDateTime endTime) {
        return questionRecordMapper.findListByTime(beginTime,endTime);
    }

    @Override
    public List<Map<String, Object>> findListByQuestionId(LocalDateTime beginTime, LocalDateTime endTime) {
        return questionRecordMapper.findListByQuestionId(beginTime,endTime);
    }

}