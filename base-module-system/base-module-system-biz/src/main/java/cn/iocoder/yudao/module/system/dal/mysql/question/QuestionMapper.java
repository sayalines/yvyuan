package cn.iocoder.yudao.module.system.dal.mysql.question;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.question.QuestionDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.question.vo.*;

/**
 * 量表管理 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface QuestionMapper extends BaseMapperX<QuestionDO> {

    default PageResult<QuestionDO> selectPage(QuestionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QuestionDO>()
                .eqIfPresent(QuestionDO::getType, reqVO.getType())
                .eqIfPresent(QuestionDO::getStatus, reqVO.getStatus())
                .eqIfPresent(QuestionDO::getIsStat, reqVO.getIsStat())
                .likeIfPresent(QuestionDO::getName, reqVO.getName())
                .likeIfPresent(QuestionDO::getDisplayName, reqVO.getDisplayName())
                .betweenIfPresent(QuestionDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(QuestionDO::getOrderNo,QuestionDO::getId));
    }

}