package cn.iocoder.yudao.module.member.dal.mysql.community;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.community.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.community.CommunityDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛文章 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface CommunityMapper extends BaseMapperX<CommunityDO> {

    default PageResult<CommunityDO> selectPage(CommunityPageReqVO reqVO) {
        LambdaQueryWrapperX<CommunityDO> wrapperX = new LambdaQueryWrapperX<CommunityDO>()
                .eqIfPresent(CommunityDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CommunityDO::getBrandId, reqVO.getBrandId())
                .likeIfPresent(CommunityDO::getTitle, reqVO.getTitle())
                .eqIfPresent(CommunityDO::getIsHot, reqVO.getIsHot())
                .eqIfPresent(CommunityDO::getIsChoice, reqVO.getIsChoice())
                .eqIfPresent(CommunityDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CommunityDO::getCreateTime, reqVO.getCreateTime());
        if (StringUtils.isNotEmpty(reqVO.getOrderBy())){
            if (reqVO.getOrderBy().equalsIgnoreCase("new")){
                wrapperX.orderByDesc(CommunityDO::getCreateTime,CommunityDO::getId);
            }else if (reqVO.getOrderBy().equalsIgnoreCase("hot")){
                wrapperX.orderByDesc(CommunityDO::getIsHot,CommunityDO::getId);
            }else if (reqVO.getOrderBy().equalsIgnoreCase("select")){
                wrapperX.orderByDesc(CommunityDO::getIsChoice,CommunityDO::getId);
            }else{
                wrapperX = wrapperX.orderByDesc(CommunityDO::getId);
            }
        }else{
            wrapperX = wrapperX.orderByDesc(CommunityDO::getId);
        }
        return selectPage(reqVO,wrapperX);
    }

}