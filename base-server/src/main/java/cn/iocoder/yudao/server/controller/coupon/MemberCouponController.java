package cn.iocoder.yudao.server.controller.coupon;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.coupon.CouponPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.template.CouponTemplatePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.coupon.AppCouponMatchReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.coupon.AppCouponPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.coupon.AppCouponTakeReqVO;
import cn.iocoder.yudao.module.promotion.convert.coupon.CouponConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import cn.iocoder.yudao.module.promotion.enums.coupon.CouponTakeTypeEnum;
import cn.iocoder.yudao.module.promotion.service.coupon.CouponService;
import cn.iocoder.yudao.module.promotion.service.coupon.CouponTemplateService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.convert.RestMemberConvert;
import cn.iocoder.yudao.server.util.ResponseResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *优惠劵 对外接口
 */
@Tag(name = "优惠劵对外接口")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/api/coupon")
public class MemberCouponController extends BaseController {

    @Resource
    private CouponService couponService;
    @Resource
    private CouponTemplateService couponTemplateService;

    /**
     * 获取优惠劵模板列表
     * @return
     */
    @PostMapping(value = "/template-list")
    @ResponseBody
    public ResponseResult getCouponTemplateList(HttpServletRequest request,Integer takeType) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        if (takeType==null){
            takeType = CouponTakeTypeEnum.USER.getValue();
        }

        if (takeType.equals(CouponTakeTypeEnum.ADMIN)){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        List<Integer> canTakeTypeList = new ArrayList<>();
        canTakeTypeList.add(takeType);

        CouponTemplatePageReqVO pageVO = new CouponTemplatePageReqVO();
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        pageVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        pageVO.setCanTakeTypes(canTakeTypeList);
        pageVO.setReleaseStartTime(LocalDateTime.now());
        pageVO.setReleaseEndTime(LocalDateTime.now());
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestMemberConvert.convertCouponTemplateList(couponTemplateService.getCouponTemplatePage(pageVO)));
        return rr;
    }


    /**
     * 领取优惠劵
     * @return
     */
    @PostMapping(value = "/take")
    @ResponseBody
    public ResponseResult takeCoupon(HttpServletRequest request, Long templateId) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        if (templateId==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        boolean canTakeAgain = true;
        Long userId = member.getId();
        AppCouponTakeReqVO reqVO = new AppCouponTakeReqVO();
        reqVO.setTemplateId(templateId);
        try{
            // 1. 领取优惠劵
            couponService.takeCoupon(reqVO.getTemplateId(), CollUtil.newHashSet(userId), CouponTakeTypeEnum.USER);

            // 2. 检查是否可以继续领取
            CouponTemplateDO couponTemplate = couponTemplateService.getCouponTemplate(reqVO.getTemplateId());
            if (couponTemplate.getTakeLimitCount() != null && couponTemplate.getTakeLimitCount() > 0) {
                Integer takeCount = couponService.getTakeCount(reqVO.getTemplateId(), userId);
                canTakeAgain = takeCount < couponTemplate.getTakeLimitCount();
            }
        }catch (Exception e){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage(e.getMessage());
            return rr;
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(canTakeAgain);
        return rr;
    }

    /**
     * 我的优惠劵列表
     * @return
     */
    @PostMapping(value = "/page")
    @ResponseBody
    public ResponseResult getCouponPage(HttpServletRequest request,String status,Integer pageNo,Integer pageSize) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        if (pageNo==null){
            pageNo = 1;
        }

        if (pageSize==null){
            pageSize = 10;
        }

        Long userId = member.getId();
        CouponPageReqVO pageReqVO = new CouponPageReqVO();
        pageReqVO.setPageNo(pageNo);
        pageReqVO.setPageSize(pageSize);
        pageReqVO.setUserIds(Collections.singleton(userId));
        if (StringUtils.isNotEmpty(status)){
            Collection<Integer> statusList = new ArrayList<>();
            for(String ss:status.split(",")){
                if (StringUtils.isNotEmpty(ss)){
                    statusList.add(Integer.valueOf(ss));
                }
            }
            pageReqVO.setStatusList(statusList);
        }
        PageResult<CouponDO> pageResult = couponService.getCouponPage(pageReqVO);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestMemberConvert.convertCouponPage(CouponConvert.INSTANCE.convertAppPage(pageResult)));
        return rr;
    }
}
