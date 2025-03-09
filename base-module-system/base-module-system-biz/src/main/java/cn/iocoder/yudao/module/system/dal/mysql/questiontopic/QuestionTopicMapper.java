package cn.iocoder.yudao.module.system.dal.mysql.questiontopic;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.questiontopic.QuestionTopicDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.questiontopic.vo.*;

/**
 * 量表题目 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface QuestionTopicMapper extends BaseMapperX<QuestionTopicDO> {

    default PageResult<QuestionTopicDO> selectPage(QuestionTopicPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QuestionTopicDO>()
                .eqIfPresent(QuestionTopicDO::getQuestionId, reqVO.getQuestionId())
                .eqIfPresent(QuestionTopicDO::getCode, reqVO.getCode())
                .likeIfPresent(QuestionTopicDO::getName, reqVO.getName())
                .betweenIfPresent(QuestionTopicDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(QuestionTopicDO::getOrderNo,QuestionTopicDO::getId));
    }

}