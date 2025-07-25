package cn.iocoder.yudao.module.promotion.service.coupon;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.coupon.CouponPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.coupon.AppCouponMatchReqVO;
import cn.iocoder.yudao.module.promotion.convert.coupon.CouponConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import cn.iocoder.yudao.module.promotion.dal.mysql.coupon.CouponMapper;
import cn.iocoder.yudao.module.promotion.enums.coupon.CouponStatusEnum;
import cn.iocoder.yudao.module.promotion.enums.coupon.CouponTakeTypeEnum;
import cn.iocoder.yudao.module.promotion.enums.coupon.CouponTemplateValidityTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.*;
import static java.util.Arrays.asList;

/**
 * 优惠劵 Service 实现类
 *
 * @author 商城管理系统
 */
@Slf4j
@Service
@Validated
public class CouponServiceImpl implements CouponService {

    @Resource
    private CouponTemplateService couponTemplateService;

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private MemberUserApi memberUserApi;

    @Override
    public CouponDO validCoupon(Long id, Long userId) {
        CouponDO coupon = couponMapper.selectByIdAndUserId(id, userId);
        if (coupon == null) {
            throw exception(COUPON_NOT_EXISTS);
        }
        validCoupon(coupon);
        return coupon;
    }

    @Override
    public void validCoupon(CouponDO coupon) {
        // 校验状态
        if (ObjectUtil.notEqual(coupon.getStatus(), CouponStatusEnum.UNUSED.getStatus())) {
            throw exception(COUPON_STATUS_NOT_UNUSED);
        }
        // 校验有效期；为避免定时器没跑，实际优惠劵已经过期
        if (!LocalDateTimeUtils.isBetween(coupon.getValidStartTime(), coupon.getValidEndTime())) {
            throw exception(COUPON_VALID_TIME_NOT_NOW);
        }
    }

    @Override
    public PageResult<CouponDO> getCouponPage(CouponPageReqVO pageReqVO) {
        // 获得用户编号
        if (StrUtil.isNotEmpty(pageReqVO.getNickname())) {
            List<MemberUserRespDTO> users = memberUserApi.getUserListByNickname(pageReqVO.getNickname());
            if (CollUtil.isEmpty(users)) {
                return PageResult.empty();
            }
            pageReqVO.setUserIds(convertSet(users, MemberUserRespDTO::getId));
        }
        // 分页查询
        return couponMapper.selectPage(pageReqVO);
    }

    @Override
    public void useCoupon(Long id, Long userId, Long orderId) {
        CouponDO coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw exception(COUPON_NOT_EXISTS);
        }

        // 校验优惠劵
        validCoupon(id, userId);

        // 更新状态
        int updateCount = couponMapper.updateByIdAndStatus(id, CouponStatusEnum.UNUSED.getStatus(),
                new CouponDO().setStatus(CouponStatusEnum.USED.getStatus())
                        .setUseOrderId(orderId).setUseTime(LocalDateTime.now()));
        if (updateCount == 0) {
            throw exception(COUPON_STATUS_NOT_UNUSED);
        }
    }

    @Override
    public void returnUsedCoupon(Long id) {
        // 校验存在
        CouponDO coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw exception(COUPON_NOT_EXISTS);
        }
        // 校验状态
        if (ObjectUtil.notEqual(coupon.getStatus(), CouponStatusEnum.USED.getStatus())) {
            throw exception(COUPON_STATUS_NOT_USED);
        }

        // 退还
        Integer status = LocalDateTimeUtils.beforeNow(coupon.getValidEndTime())
                ? CouponStatusEnum.EXPIRE.getStatus() // 退还时可能已经过期了
                : CouponStatusEnum.UNUSED.getStatus();
        int updateCount = couponMapper.updateByIdAndStatus(id, CouponStatusEnum.USED.getStatus(),
                new CouponDO().setStatus(status));
        if (updateCount == 0) {
            throw exception(COUPON_STATUS_NOT_USED);
        }
        // TODO 增加优惠券变动记录？
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        // 校验存在
        validateCouponExists(id);

        // 更新优惠劵
        int deleteCount = couponMapper.delete(id,
                asList(CouponStatusEnum.UNUSED.getStatus(), CouponStatusEnum.EXPIRE.getStatus()));
        if (deleteCount == 0) {
            throw exception(COUPON_DELETE_FAIL_USED);
        }
        // 减少优惠劵模板的领取数量 -1
        couponTemplateService.updateCouponTemplateTakeCount(id, -1);
    }

    @Override
    public List<CouponDO> getCouponList(Long userId, Integer status) {
        return couponMapper.selectListByUserIdAndStatus(userId, status);
    }

    private void validateCouponExists(Long id) {
        if (couponMapper.selectById(id) == null) {
            throw exception(COUPON_NOT_EXISTS);
        }
    }

    @Override
    public Long getUnusedCouponCount(Long userId) {
        return couponMapper.selectCountByUserIdAndStatus(userId, CouponStatusEnum.UNUSED.getStatus());
    }

    @Override
    public void takeCoupon(Long templateId, Set<Long> userIds, CouponTakeTypeEnum takeType) {
        CouponTemplateDO template = couponTemplateService.getCouponTemplate(templateId);
        // 1. 过滤掉达到领取限制的用户
        removeTakeLimitUser(userIds, template);
        // 2. 校验优惠劵是否可以领取
        validateCouponTemplateCanTake(template, userIds, takeType);

        // 3. 批量保存优惠劵
        couponMapper.insertBatch(convertList(userIds, userId -> CouponConvert.INSTANCE.convert(template, userId)));

        // 3. 增加优惠劵模板的领取数量
        couponTemplateService.updateCouponTemplateTakeCount(templateId, userIds.size());
    }

    @Override
    public CouponDO takeCoupon(Long templateId, Long userId, CouponTakeTypeEnum takeType) {
        CouponTemplateDO template = couponTemplateService.getCouponTemplate(templateId);
        // 1. 过滤掉达到领取限制的用户
        removeTakeLimitUser(CollUtil.newHashSet(userId), template);
        // 2. 校验优惠劵是否可以领取
        validateCouponTemplateCanTake(template, CollUtil.newHashSet(userId), takeType);

        // 3. 保存优惠劵
        CouponDO couponDO = CouponConvert.INSTANCE.convert(template, userId);
        couponMapper.insert(couponDO);

        // 3. 增加优惠劵模板的领取数量
        couponTemplateService.updateCouponTemplateTakeCount(templateId, 1);

        return couponDO;
    }

    @Override
    public Long takeCoupon(Long templateId, Long userId, String exchangeCode, CouponTakeTypeEnum takeType) {
        CouponTemplateDO template = couponTemplateService.getCouponTemplate(templateId);
        // 1. 过滤掉达到领取限制的用户
        removeTakeLimitUser(CollUtil.newHashSet(userId), template);
        // 2. 校验优惠劵是否可以领取
        validateCouponTemplateCanTake(template, CollUtil.newHashSet(userId), takeType);

        // 3. 保存优惠劵
        CouponDO couponDO = CouponConvert.INSTANCE.convert(template, userId,exchangeCode);
        couponMapper.insert(couponDO);

        // 3. 增加优惠劵模板的领取数量
        couponTemplateService.updateCouponTemplateTakeCount(templateId, 1);

        return couponDO.getId();
    }

    @Override
    public Long takeCoupon(Long templateId, Long userId, Long activityId, CouponTakeTypeEnum takeType) {
        CouponTemplateDO template = couponTemplateService.getCouponTemplate(templateId);
        // 1. 过滤掉达到领取限制的用户
        removeTakeLimitUser(CollUtil.newHashSet(userId), template);
        // 2. 校验优惠劵是否可以领取
        validateCouponTemplateCanTake(template, CollUtil.newHashSet(userId), takeType);

        // 3. 保存优惠劵
        CouponDO couponDO = CouponConvert.INSTANCE.convert(template, userId,activityId);
        couponMapper.insert(couponDO);

        // 3. 增加优惠劵模板的领取数量
        couponTemplateService.updateCouponTemplateTakeCount(templateId, 1);

        return couponDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void takeCouponByRegister(Long userId) {
        List<CouponTemplateDO> templates = couponTemplateService.getCouponTemplateListByTakeType(CouponTakeTypeEnum.REGISTER);
        for (CouponTemplateDO template : templates) {
            takeCoupon(template.getId(), CollUtil.newHashSet(userId), CouponTakeTypeEnum.REGISTER);
        }
    }

    @Override
    public Map<Long, Integer> getTakeCountMapByTemplateIds(Collection<Long> templateIds, Long userId) {
        if (CollUtil.isEmpty(templateIds)) {
            return Collections.emptyMap();
        }
        return couponMapper.selectCountByUserIdAndTemplateIdIn(userId, templateIds);
    }

    @Override
    public List<CouponDO> getMatchCouponList(Long userId, AppCouponMatchReqVO matchReqVO) {
        return couponMapper.selectListByUserIdAndStatusAndUsePriceLeAndProductScope(userId,
                CouponStatusEnum.UNUSED.getStatus(),
                matchReqVO.getPrice(), matchReqVO.getSpuIds(), matchReqVO.getCategoryIds());
    }

    @Override
    public int expireCoupon() {
        // 1. 查询待过期的优惠券
        List<CouponDO> list = couponMapper.selectListByStatusAndValidEndTimeLe(
                CouponStatusEnum.UNUSED.getStatus(), LocalDateTime.now());
        if (CollUtil.isEmpty(list)) {
            return 0;
        }

        // 2. 遍历执行
        int count = 0;
        for (CouponDO coupon : list) {
            try {
                boolean success = expireCoupon(coupon);
                if (success) {
                    count++;
                }
            } catch (Exception e) {
                log.error("[expireCoupon][coupon({}) 更新为已过期失败]", coupon.getId(), e);
            }
        }
        return count;
    }

    @Override
    public Map<Long, Boolean> getUserCanCanTakeMap(Long userId, List<CouponTemplateDO> templates) {
        // 1. 未登录时，都显示可以领取
        Map<Long, Boolean> userCanTakeMap = convertMap(templates, CouponTemplateDO::getId, templateId -> true);
        if (userId == null) {
            return userCanTakeMap;
        }

        // 2.1 过滤领取数量无限制的
        Set<Long> templateIds = convertSet(templates, CouponTemplateDO::getId, template -> template.getTakeLimitCount() != -1);
        // 2.2 检查用户领取的数量是否超过限制
        if (CollUtil.isNotEmpty(templateIds)) {
            Map<Long, Integer> couponTakeCountMap = this.getTakeCountMapByTemplateIds(templateIds, userId);
            for (CouponTemplateDO template : templates) {
                Integer takeCount = couponTakeCountMap.get(template.getId());
                userCanTakeMap.put(template.getId(), takeCount == null || takeCount < template.getTakeLimitCount());
            }
        }
        return userCanTakeMap;
    }

    @Override
    public CouponDO getCoupon(Long id) {
        return couponMapper.selectById(id);
    }

    /**
     * 过期单个优惠劵
     *
     * @param coupon 优惠劵
     * @return 是否过期成功
     */
    private boolean expireCoupon(CouponDO coupon) {
        // 更新记录状态
        int updateRows = couponMapper.updateByIdAndStatus(coupon.getId(), CouponStatusEnum.UNUSED.getStatus(),
                new CouponDO().setStatus(CouponStatusEnum.EXPIRE.getStatus()));
        if (updateRows == 0) {
            log.error("[expireCoupon][coupon({}) 更新为已过期失败]", coupon.getId());
            return false;
        }
        log.info("[expireCoupon][coupon({}) 更新为已过期成功]", coupon.getId());
        return true;
    }

    /**
     * 校验优惠券是否可以领取
     *
     * @param couponTemplate 优惠券模板
     * @param userIds        领取人列表
     * @param takeType       领取方式
     */
    private void validateCouponTemplateCanTake(CouponTemplateDO couponTemplate, Set<Long> userIds, CouponTakeTypeEnum takeType) {
        // 如果所有用户都领取过，则抛出异常
        if (CollUtil.isEmpty(userIds)) {
            throw exception(COUPON_TEMPLATE_USER_ALREADY_TAKE);
        }

        // 校验模板
        if (couponTemplate == null) {
            throw exception(COUPON_TEMPLATE_NOT_EXISTS);
        }
        // 校验剩余数量
        if (!couponTemplate.getTotalCount().equals(-1)){
            if (couponTemplate.getTakeCount() + userIds.size() > couponTemplate.getTotalCount()) {
                throw exception(COUPON_TEMPLATE_NOT_ENOUGH);
            }
        }

        // 校验"固定日期"的有效期类型是否过期
        if (CouponTemplateValidityTypeEnum.DATE.getType().equals(couponTemplate.getValidityType())) {
            if (LocalDateTimeUtils.beforeNow(couponTemplate.getValidEndTime())) {
                throw exception(COUPON_TEMPLATE_EXPIRED);
            }
        }
        // 校验领取方式
        if (ObjectUtil.notEqual(couponTemplate.getTakeType(), takeType.getValue())) {
            throw exception(COUPON_TEMPLATE_CANNOT_TAKE);
        }
    }

    /**
     * 过滤掉达到领取上线的用户
     *
     * @param userIds        用户编号数组
     * @param couponTemplate 优惠劵模版
     */
    private void removeTakeLimitUser(Set<Long> userIds, CouponTemplateDO couponTemplate) {
        if (couponTemplate.getTakeLimitCount() <= 0) {
            return;
        }
        // 查询已领过券的用户
        List<CouponDO> alreadyTakeCoupons = couponMapper.selectListByTemplateIdAndUserId(couponTemplate.getId(), userIds);
        if (CollUtil.isEmpty(alreadyTakeCoupons)) {
            return;
        }
        // 移除达到领取限制的用户
        Map<Long, Integer> userTakeCountMap = CollStreamUtil.groupBy(alreadyTakeCoupons, CouponDO::getUserId, Collectors.summingInt(c -> 1));
        userIds.removeIf(userId -> MapUtil.getInt(userTakeCountMap, userId, 0) >= couponTemplate.getTakeLimitCount());
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private CouponServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }
}
