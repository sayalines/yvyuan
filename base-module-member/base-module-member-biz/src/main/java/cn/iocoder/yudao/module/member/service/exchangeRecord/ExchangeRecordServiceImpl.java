package cn.iocoder.yudao.module.member.service.exchangeRecord;

import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.config.MemberConfigDO;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfig.ExchangeConfigDO;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfigItem.ExchangeConfigItemDO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.config.MemberConfigService;
import cn.iocoder.yudao.module.member.service.exchangeConfig.ExchangeConfigService;
import cn.iocoder.yudao.module.member.service.exchangeConfigItem.ExchangeConfigItemService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.product.api.sku.ProductSkuApi;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import cn.iocoder.yudao.module.promotion.enums.coupon.CouponTakeTypeEnum;
import cn.iocoder.yudao.module.promotion.service.coupon.CouponService;
import cn.iocoder.yudao.module.promotion.service.coupon.CouponTemplateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.exchangeRecord.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeRecord.ExchangeRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.exchangeRecord.ExchangeRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.COUPON_TEMPLATE_NOT_EXISTS;

/**
 * 会员兑换记录 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class ExchangeRecordServiceImpl implements ExchangeRecordService {

    @Resource
    private ExchangeRecordMapper exchangeRecordMapper;
    @Resource
    private MemberUserService memberUserService;

    @Resource
    private ExchangeConfigService exchangeConfigService;
    @Resource
    private ExchangeConfigItemService exchangeConfigItemService;
    @Resource
    private CouponTemplateService couponTemplateService;
    @Resource
    private CouponService couponService;

    @Resource
    private ProductSkuApi productSkuApi;

    @Resource
    private MemberConfigService memberConfigService;


    @Override
    public Long createExchangeRecord(ExchangeRecordSaveReqVO createReqVO) {
        // 插入
        ExchangeRecordDO exchangeRecord = BeanUtils.toBean(createReqVO, ExchangeRecordDO.class);
        exchangeRecordMapper.insert(exchangeRecord);
        // 返回
        return exchangeRecord.getId();
    }

    @Override
    public void updateExchangeRecord(ExchangeRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateExchangeRecordExists(updateReqVO.getId());
        // 更新
        ExchangeRecordDO updateObj = BeanUtils.toBean(updateReqVO, ExchangeRecordDO.class);
        exchangeRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteExchangeRecord(Long id) {
        // 校验存在
        validateExchangeRecordExists(id);
        // 删除
        exchangeRecordMapper.deleteById(id);
    }

    private void validateExchangeRecordExists(Long id) {
        if (exchangeRecordMapper.selectById(id) == null) {
            throw exception(EXCHANGE_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public ExchangeRecordDO getExchangeRecord(Long id) {
        return exchangeRecordMapper.selectById(id);
    }

    @Override
    public PageResult<ExchangeRecordRespVO> getExchangeRecordPage(ExchangeRecordPageReqVO pageReqVO) {
        // 根据用户昵称查询出用户 ids
        Set<Long> userIds = null;
        if (StringUtils.isNotBlank(pageReqVO.getNickname())) {
            List<MemberUserDO> users = memberUserService.getUserListByNickname(pageReqVO.getNickname());
            // 如果查询用户结果为空直接返回无需继续查询
            if (CollectionUtils.isEmpty(users)) {
                return PageResult.empty();
            }
            userIds = convertSet(users, MemberUserDO::getId);
        }

        PageResult<ExchangeRecordRespVO> page = BeanUtils.toBean(exchangeRecordMapper.selectPage(pageReqVO,userIds), ExchangeRecordRespVO.class);
        if (page!=null && page.getList().size()>0){
            // user 拼接
            Set<Long> idsSet = convertSet(page.getList(), ExchangeRecordRespVO::getUserId);
            if (idsSet.size()>0){
                List<MemberUserDO> users = memberUserService.getUserList(idsSet);
                Map<Long, MemberUserDO> userMap = convertMap(users, MemberUserDO::getId);
                page.getList().forEach(record -> MapUtils.findAndThen(userMap, record.getUserId(),
                        memberUserRespDTO -> record.setNickname(memberUserRespDTO.getNickname())));
            }

        }
        return page;
    }

    @Override
    public Integer getExchangeRecordListByCode(String code) {
        if (StringUtils.isEmpty(code)){
            return 0;
        }
        List<ExchangeRecordDO> list  = exchangeRecordMapper.selectList(ExchangeRecordDO::getCode,code);
        if (list==null){
            list = new ArrayList<>();
        }
        return list.size();
    }

    @Override
    public ExchangeRecordDO takeCode(Long userId, String code) {
        if (userId==null ||StringUtils.isEmpty(code)){
            throw exception(EXCHANGE_RECORD_NOT_PARAM);
        }
        MemberUserDO member = memberUserService.getUser(userId);
        if (member==null){
            throw exception(USER_NOT_EXISTS);
        }

        ExchangeConfigItemDO configItemDO = exchangeConfigItemService.getExchangeConfigItemByCode(code);
        if (configItemDO==null){
            throw exception(EXCHANGE_RECORD_ERROR_1);
        }

        ExchangeConfigDO configDO = exchangeConfigService.getExchangeConfig(configItemDO.getConfigId());
        if (configDO==null){
            throw exception(EXCHANGE_CONFIG_NOT_EXISTS);
        }
        //排除黑名单用户
        String blackUsers = configDO.getBlackUsers();
        if (StringUtils.isNotEmpty(blackUsers)){
            Boolean isOk = true;
            for(String ss:blackUsers.split(",")){
                if (ss.equalsIgnoreCase(String.valueOf(userId))){
                    isOk = false;
                    break;
                }
            }

            if (!isOk){
                throw exception(EXCHANGE_RECORD_ERROR_1);
            }
        }
        //判断是否在白名单用户列表内
        String whiteUsers = configDO.getWhiteUsers();
        if (StringUtils.isNotEmpty(whiteUsers)){
            Boolean isOk = false;
            for(String ss:whiteUsers.split(",")){
                if (ss.equalsIgnoreCase(String.valueOf(userId))){
                    isOk = true;
                    break;
                }
            }

            if (!isOk){
                throw exception(EXCHANGE_RECORD_ERROR_1);
            }
        }
        //兑换上限
        Integer dayCount = configDO.getDayCount();
        if (dayCount!=null && dayCount>0){
            Integer tmpCount = getUserExchangeRecordCount(userId,configDO.getId()).intValue();
            if (dayCount.compareTo(tmpCount)<=0){
                throw exception(EXCHANGE_RECORD_ERROR_9);
            }
        }

        if (configItemDO.getValidStartTime()!=null && configItemDO.getValidStartTime().isAfter(LocalDateTime.now())){
            throw exception(EXCHANGE_RECORD_ERROR_2);
        }

        if (configItemDO.getValidEndTime()!=null && configItemDO.getValidEndTime().isBefore(LocalDateTime.now())){
            throw exception(EXCHANGE_RECORD_ERROR_3);
        }

        Integer canUseCount = configItemDO.getTotalCount() - configItemDO.getUseCount();
        Integer useCount = getExchangeRecordListByCode(code);
        if (canUseCount-useCount<=0){
            if (configItemDO.getTotalCount().equals(1)){
                throw exception(EXCHANGE_RECORD_ERROR_4);
            }else{
                throw exception(EXCHANGE_RECORD_ERROR_5);
            }
        }

        ExchangeRecordDO result = new ExchangeRecordDO();
        result.setUserId(member.getId());
        result.setConfigId(configItemDO.getConfigId());
        result.setConfigItemId(configItemDO.getId());
        result.setCode(configItemDO.getCode());
        result.setBizType(configItemDO.getBizType());
        result.setBizId(configItemDO.getBizId());
        result.setActiveTime(LocalDateTime.now());
        result.setIsUse(false);
        Integer bizType = configItemDO.getBizType();
        if (bizType.equals(1) || bizType.equals(4) ){
            //商品兑换/周边产品兑换
            if (configItemDO.getBizId()==null){
                throw exception(CARD_EXCHANGE_CONFIG_ERROR);
            }
            ProductSkuRespDTO sku = productSkuApi.getSku(configItemDO.getBizId());
            if (sku==null){
                throw exception(CARD_EXCHANGE_CONFIG_ERROR);
            }
            result.setValidStartTime(configItemDO.getValidStartTime());
            result.setValidEndTime(configItemDO.getValidEndTime());
        }else if (bizType.equals(2) ){
            //优惠劵兑换
            //插入优惠劵
            if (configItemDO.getBizId()==null){
                throw exception(CARD_EXCHANGE_CONFIG_ERROR);
            }
            CouponTemplateDO template = couponTemplateService.getCouponTemplate(configItemDO.getBizId());
            if (template==null){
                throw exception(COUPON_TEMPLATE_NOT_EXISTS);
            }
            //领取优惠劵
            Long couponId = couponService.takeCoupon(configItemDO.getBizId(), userId,configItemDO.getCode(), CouponTakeTypeEnum.ADMIN);
            result.setResultId(couponId);
            //固定日期
            if (template.getValidityType().equals(1)){
                result.setValidStartTime(template.getValidStartTime());
                result.setValidEndTime(template.getValidEndTime());
            }else {
                //领取之后
                Integer fixedStartTerm =  template.getFixedStartTerm();
                Integer fixedEndTerm =  template.getFixedEndTerm();

                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();

                calendar.setTime(currentDate);
                calendar.add(Calendar.DAY_OF_MONTH,fixedStartTerm);
                Date validStartTime = calendar.getTime();

                calendar.setTime(currentDate);
                calendar.add(Calendar.DAY_OF_MONTH,fixedEndTerm);
                Date validEndTime = calendar.getTime();

                result.setValidStartTime(toFormatLocalDateTime(validStartTime));
                result.setValidEndTime(toFormatLocalDateTime(validEndTime));
            }
        }
//        else if (bizType.equals(3)){
//            //功能卡兑换
//            CardExchangeDO cardExchangeDO = cardExchangeService.getCardExchange(configItemDO.getBizId());
//            if (cardExchangeDO==null){
//                throw exception(CARD_EXCHANGE_NOT_EXISTS);
//            }
//
//            MemberCardRecordDO entity = new MemberCardRecordDO();
//            entity.setBizType(cardExchangeDO.getBizType());
//            entity.setUserId(userId);
//            entity.setCardId(cardExchangeDO.getId());
//            entity.setCardName(cardExchangeDO.getTitle());
//            entity.setCardLogo(cardExchangeDO.getLogo());
//            entity.setIsUse(false);
//            entity.setIsExchange(true);
//            entity.setExchangeCode(configItemDO.getCode());
//            //固定日期
//            if (cardExchangeDO.getValidityType().equals(1)){
//                entity.setValidStartTime(cardExchangeDO.getValidStartTime());
//                entity.setValidEndTime(cardExchangeDO.getValidEndTime());
//            }else {
//                //领取之后
//                Integer fixedStartTerm =  cardExchangeDO.getFixedStartTerm();
//                Integer fixedEndTerm =  cardExchangeDO.getFixedEndTerm();
//
//                Calendar calendar = Calendar.getInstance();
//                Date currentDate = calendar.getTime();
//
//                calendar.setTime(currentDate);
//                calendar.add(Calendar.DAY_OF_MONTH,fixedStartTerm);
//                Date validStartTime = calendar.getTime();
//
//                calendar.setTime(currentDate);
//                calendar.add(Calendar.DAY_OF_MONTH,fixedEndTerm);
//                Date validEndTime = calendar.getTime();
//
//                entity.setValidStartTime(toFormatLocalDateTime(validStartTime));
//                entity.setValidEndTime(toFormatLocalDateTime(validEndTime));
//            }
//            cardRecordMapper.insert(entity);
//            result.setValidStartTime(entity.getValidStartTime());
//            result.setValidEndTime(entity.getValidEndTime());
//            result.setResultId(entity.getId());
//        }
        exchangeRecordMapper.insert(result);
        return result;

    }

    @Override
    public ExchangeRecordDO getUserCanUseExchangeRecord(Long userId, String code) {
        LambdaQueryWrapper<ExchangeRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(ExchangeRecordDO::getUserId,userId)
                .eq(ExchangeRecordDO::getCode,code)
                .eq(ExchangeRecordDO::getIsUse,false)
                .orderByDesc(ExchangeRecordDO::getId);
        List<ExchangeRecordDO> list = exchangeRecordMapper.selectList(wrapper);
        if (list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void useExchangeRecord(Long userId, String code) {
        ExchangeRecordDO exchangeRecord = getUserCanUseExchangeRecord(userId,code);
        if (exchangeRecord==null){
            throw exception(EXCHANGE_RECORD_ERROR_1);
        }
        exchangeRecord.setIsUse(true);
        exchangeRecord.setUseTime(LocalDateTime.now());
        exchangeRecordMapper.updateById(exchangeRecord);

        ExchangeConfigItemDO configItemDO = exchangeConfigItemService.getExchangeConfigItem(exchangeRecord.getConfigItemId());
        if (configItemDO==null){
            throw exception(EXCHANGE_RECORD_ERROR_1);
        }

        configItemDO.setUseCount(configItemDO.getUseCount()+1);
        exchangeConfigItemService.updateExchangeConfigItem(configItemDO);
    }

    @Override
    public void cancelExchangeRecord(Long userId, String code) {
        ExchangeRecordDO exchangeRecord = null;
        LambdaQueryWrapper<ExchangeRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(ExchangeRecordDO::getUserId,userId)
                .eq(ExchangeRecordDO::getCode,code)
                .eq(ExchangeRecordDO::getIsUse,true)
                .orderByDesc(ExchangeRecordDO::getUpdateTime);
        List<ExchangeRecordDO> list = exchangeRecordMapper.selectList(wrapper);
        if (list!=null && list.size()>0){
            exchangeRecord = list.get(0);
        }

        if (exchangeRecord==null){
            throw exception(EXCHANGE_RECORD_ERROR_6);
        }

        exchangeRecord.setIsUse(false);
        exchangeRecord.setUseTime(null);
        exchangeRecordMapper.updateById(exchangeRecord);

        ExchangeConfigItemDO configItemDO = exchangeConfigItemService.getExchangeConfigItem(exchangeRecord.getConfigItemId());
        if (configItemDO==null){
            throw exception(EXCHANGE_RECORD_ERROR_1);
        }

        configItemDO.setUseCount(configItemDO.getUseCount()-1);
        exchangeConfigItemService.updateExchangeConfigItem(configItemDO);
    }

    @Override
    public Long getUserExchangeRecordCountByToday(Long userId) {
        LocalDateTime startTime = formatLocalDateTime(LocalDateTime.now(),0);
        LocalDateTime endTime = formatLocalDateTime(LocalDateTime.now(),1);
        LambdaQueryWrapper<ExchangeRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(ExchangeRecordDO::getUserId,userId)
                .ge(ExchangeRecordDO::getCreateTime,startTime)
                .le(ExchangeRecordDO::getCreateTime,endTime);
        Long count = exchangeRecordMapper.selectCount(wrapper);
        if (count==null){
            count = 0L;
        }
        return count;
    }

    @Override
    public Long getUserExchangeRecordCount(Long userId, Long configId) {
        LambdaQueryWrapper<ExchangeRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(ExchangeRecordDO::getUserId,userId)
                .eq(ExchangeRecordDO::getConfigId,configId);
        Long count = exchangeRecordMapper.selectCount(wrapper);
        if (count==null){
            count = 0L;
        }
        return count;
    }


    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public LocalDateTime formatLocalDateTime(LocalDateTime dateTime,Integer type){
        LocalDateTime result = null;
        if (dateTime!=null){
            String formatter = "";
            if (type==0){
                formatter = "yyyy-MM-dd 00:00:00";
            }else{
                formatter = "yyyy-MM-dd 23:59:59";
            }
            String result01 = dateTime.format(DateTimeFormatter.ofPattern(formatter));

            formatter = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
            result = LocalDateTime.parse(result01,df);
        }
        return result;
    }


    /**
     * 格式化时间
     * @param value
     * @return
     */
    public LocalDateTime toFormatLocalDateTime(Date value){
        LocalDateTime result = null;
        if (value!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            result = LocalDateTime.parse(sdf.format(value),df);
        }
        return result;
    }

}