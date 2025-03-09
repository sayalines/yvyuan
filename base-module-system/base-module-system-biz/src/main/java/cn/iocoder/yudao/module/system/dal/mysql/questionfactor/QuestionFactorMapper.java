package cn.iocoder.yudao.module.system.dal.mysql.questionfactor;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.questionfactor.QuestionFactorDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.questionfactor.vo.*;

/**
 * 量表因子 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface QuestionFactorMapper extends BaseMapperX<QuestionFactorDO> {

    default PageResult<QuestionFactorDO> selectPage(QuestionFactorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QuestionFactorDO>()
                .eqIfPresent(QuestionFactorDO::getQuestionId, reqVO.getQuestionId())
                .likeIfPresent(QuestionFactorDO::getName, reqVO.getName())
                .betweenIfPresent(QuestionFactorDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(QuestionFactorDO::getOrderNo,QuestionFactorDO::getId));
    }

}