package cn.iocoder.yudao.server.convert;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.controller.app.signin.vo.record.AppMemberSignInRecordRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.point.MemberPointRecordDO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.coupon.AppCouponRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.vo.*;

import java.util.ArrayList;
import java.util.List;


public class RestMemberConvert {

    public static PageResult<ApiPointDO> convertPointPage(PageResult<MemberPointRecordDO> pageResult){
        PageResult<ApiPointDO> resultPage =new PageResult<>();
        if (pageResult!=null){
            resultPage.setTotal(pageResult.getTotal());
            List<ApiPointDO> list = new ArrayList<>();
            if (pageResult.getList()!=null){
                for(MemberPointRecordDO dd:pageResult.getList()){
                    ApiPointDO dto = new ApiPointDO();
                    dto.setId(dd.getId());
                    dto.setTitle(dd.getTitle());
                    dto.setDescription(dd.getDescription());
                    dto.setBizType(dd.getBizType());
                    dto.setBizTypeDesc(MemberPointBizTypeEnum.getByType(dd.getBizType()).getName());
                    dto.setCreateTime(CommonUtils.formatLocalDateTime(dd.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
                    dto.setPoint(dd.getPoint());
                    dto.setTotalPoint(dd.getTotalPoint());
                    list.add(dto);
                }
            }
            resultPage.setList(list);
        }
        return resultPage;
    }


    public static ApiMemberSignInRecordDO convertSignInRecord(AppMemberSignInRecordRespVO respVO){
        ApiMemberSignInRecordDO result =new ApiMemberSignInRecordDO();
        if (respVO!=null){
            result.setDay(respVO.getDay());
            result.setPoint(respVO.getPoint());
            result.setExperience(respVO.getExperience());
            result.setCreateTime(CommonUtils.formatLocalDateTime(respVO.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        return result;
    }



    public static List<ApiCouponTemplateDO> convertCouponTemplateList(PageResult<CouponTemplateDO> page){
        List<ApiCouponTemplateDO> resultList =new ArrayList<>();
        if (page!=null && page.getList().size()>0){
            for(CouponTemplateDO dd:page.getList()){
                ApiCouponTemplateDO dto = BeanUtils.toBean(dd,ApiCouponTemplateDO.class);
                dto.setDiscountPrice(CommonUtils.formatPrice(dd.getDiscountPrice()));
                dto.setDiscountLimitPrice(CommonUtils.formatPrice(dd.getDiscountLimitPrice()));
                dto.setUsePrice(CommonUtils.formatPrice(dd.getUsePrice()));
                dto.setValidStartTime(CommonUtils.formatLocalDateTime(dd.getValidStartTime(),"yyyy-MM-dd HH:mm:ss"));
                dto.setValidEndTime(CommonUtils.formatLocalDateTime(dd.getValidEndTime(),"yyyy-MM-dd HH:mm:ss"));
                resultList.add(dto);
            }
        }
        return resultList;
    }

    public static PageResult<ApiCouponDO> convertCouponPage(PageResult<AppCouponRespVO> page){
        PageResult<ApiCouponDO>  result =new PageResult<>();
        result.setTotal(0L);
        List<ApiCouponDO> list = new ArrayList<>();
        if (page!=null && page.getList().size()>0){
            result.setTotal(page.getTotal());
            for(AppCouponRespVO dd:page.getList()){
                ApiCouponDO dto = BeanUtils.toBean(dd,ApiCouponDO.class);
                dto.setDiscountPrice(CommonUtils.formatPrice(dd.getDiscountPrice()));
                dto.setDiscountLimitPrice(CommonUtils.formatPrice(dd.getDiscountLimitPrice()));
                dto.setUsePrice(CommonUtils.formatPrice(dd.getUsePrice()));
                dto.setValidStartTime(CommonUtils.formatLocalDateTime(dd.getValidStartTime(),"yyyy-MM-dd HH:mm:ss"));
                dto.setValidEndTime(CommonUtils.formatLocalDateTime(dd.getValidEndTime(),"yyyy-MM-dd HH:mm:ss"));
                list.add(dto);
            }
        }
        result.setList(list);
        return result;
    }

    public static ApiCouponDO convertCoupon(CouponDO coupon){
        ApiCouponDO dto = BeanUtils.toBean(coupon,ApiCouponDO.class);
        dto.setDiscountPrice(CommonUtils.formatPrice(coupon.getDiscountPrice()));
        dto.setDiscountLimitPrice(CommonUtils.formatPrice(coupon.getDiscountLimitPrice()));
        dto.setUsePrice(CommonUtils.formatPrice(coupon.getUsePrice()));
        dto.setValidStartTime(CommonUtils.formatLocalDateTime(coupon.getValidStartTime(),"yyyy-MM-dd HH:mm:ss"));
        dto.setValidEndTime(CommonUtils.formatLocalDateTime(coupon.getValidEndTime(),"yyyy-MM-dd HH:mm:ss"));
        return dto;
    }
}
