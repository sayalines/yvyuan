package cn.iocoder.yudao.module.member.dal.mysql.feedback;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.feedback.MemberFeedbackDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.feedback.vo.*;

/**
 * 问题反馈 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface MemberFeedbackMapper extends BaseMapperX<MemberFeedbackDO> {

    default PageResult<MemberFeedbackDO> selectPage(MemberFeedbackPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberFeedbackDO>()
                .eqIfPresent(MemberFeedbackDO::getId, reqVO.getId())
                .eqIfPresent(MemberFeedbackDO::getUserId, reqVO.getUserId())
                .likeIfPresent(MemberFeedbackDO::getUserName, reqVO.getUserName())
                .likeIfPresent(MemberFeedbackDO::getMobile, reqVO.getMobile())
                .likeIfPresent(MemberFeedbackDO::getContent, reqVO.getContent())
                .eqIfPresent(MemberFeedbackDO::getIfSolve, reqVO.getIfSolve())
                .betweenIfPresent(MemberFeedbackDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberFeedbackDO::getId));
    }

}