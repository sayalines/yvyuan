package cn.iocoder.yudao.module.system.dal.mysql.questiondimension;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.questiondimension.QuestionDimensionDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.questiondimension.vo.*;

/**
 * 量表维度 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface QuestionDimensionMapper extends BaseMapperX<QuestionDimensionDO> {

    default PageResult<QuestionDimensionDO> selectPage(QuestionDimensionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QuestionDimensionDO>()
                .eqIfPresent(QuestionDimensionDO::getQuestionId, reqVO.getQuestionId())
                .likeIfPresent(QuestionDimensionDO::getName, reqVO.getName())
                .betweenIfPresent(QuestionDimensionDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(QuestionDimensionDO::getOrderNo,QuestionDimensionDO::getId));
    }

}