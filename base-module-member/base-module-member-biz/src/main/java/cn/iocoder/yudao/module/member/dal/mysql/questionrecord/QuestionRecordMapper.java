package cn.iocoder.yudao.module.member.dal.mysql.questionrecord;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.questionrecord.QuestionRecordDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.questionrecord.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 问卷记录 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface QuestionRecordMapper extends BaseMapperX<QuestionRecordDO> {

    default PageResult<QuestionRecordDO> selectPage(QuestionRecordPageReqVO reqVO, Set<Long> userIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QuestionRecordDO>()
                .eqIfPresent(QuestionRecordDO::getUserId, reqVO.getUserId())
                .inIfPresent(QuestionRecordDO::getUserId, userIds)
                .eqIfPresent(QuestionRecordDO::getQuestionId, reqVO.getQuestionId())
                .eqIfPresent(QuestionRecordDO::getAnswer, reqVO.getAnswer())
                .betweenIfPresent(QuestionRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(QuestionRecordDO::getId));
    }

    /**
     * 按年各维度重点关注人数对比
     * @return
     */
    List<Map<String,Object>> findListByTime(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);
    /**
     * 四个指定量表的测评结果各维度的人数占比情况
     * @return
     */
    List<Map<String,Object>> findListByQuestionId(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);
}