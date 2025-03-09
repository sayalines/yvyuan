package cn.iocoder.yudao.server.controller.trade;

import cn.iocoder.yudao.framework.common.enums.TerminalEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.api.address.MemberAddressApi;
import cn.iocoder.yudao.module.member.api.address.dto.MemberAddressRespDTO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.yudao.module.pay.controller.admin.order.vo.PayOrderSubmitRespVO;
import cn.iocoder.yudao.module.pay.controller.app.order.vo.AppPayOrderSubmitReqVO;
import cn.iocoder.yudao.module.pay.convert.order.PayOrderConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.product.api.sku.ProductSkuApi;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.yudao.module.product.api.spu.ProductSpuApi;
import cn.iocoder.yudao.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.yudao.module.product.dal.dataobject.category.ProductCategoryDO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.yudao.module.product.service.category.ProductCategoryService;
import cn.iocoder.yudao.module.product.service.spu.ProductSpuService;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.coupon.AppCouponMatchReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.coupon.AppCouponMatchRespVO;
import cn.iocoder.yudao.module.promotion.convert.coupon.CouponConvert;
import cn.iocoder.yudao.module.promotion.service.coupon.CouponService;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.yudao.module.trade.controller.app.base.property.AppProductPropertyValueDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.*;
import cn.iocoder.yudao.module.trade.convert.order.TradeOrderConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.cart.CartDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.delivery.DeliveryExpressDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.enums.delivery.DeliveryTypeEnum;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderStatusEnum;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderTypeEnum;
import cn.iocoder.yudao.module.trade.framework.order.config.TradeOrderProperties;
import cn.iocoder.yudao.module.trade.service.cart.CartService;
import cn.iocoder.yudao.module.trade.service.delivery.DeliveryExpressService;
import cn.iocoder.yudao.module.trade.service.delivery.DeliveryExpressTemplateService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderQueryService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderUpdateService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.convert.RestOrderConvert;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.util.ResponseResult;
import cn.iocoder.yudao.server.vo.ApiTradeOrderDetailDO;
import cn.iocoder.yudao.server.vo.ApiTradeOrderSettlementDO;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;


/**
 * 交易订单对外接口
 */
@Tag(name = "对外接口")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/api/trade/order")
public class OrderController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;
    @Resource
    private TradeOrderQueryService tradeOrderQueryService;
    @Resource
    private DeliveryExpressService deliveryExpressService;
    @Resource
    private DeliveryExpressTemplateService deliveryExpressTemplateService;
    @Resource
    private TradeOrderProperties tradeOrderProperties;
    @Resource
    private CartService cartService;
    @Resource
    private ProductSpuService productSpuService;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private CouponService couponService;
    @Resource
    private ProductCategoryService categoryService;
    @Resource
    private MemberAddressApi addressApi;
    @Resource
    private ProductSpuApi productSpuApi;
    @Resource
    private ProductSkuApi productSkuApi;

    @Value("${plat.spec-spu-ids}")
    private String specSpuIds;

    @Value("${plat.spec-spu-qty}")
    private Integer specSpuQty;

    @Value("${plat.spec-spu-remark}")
    private String specSpuRemark;

    /**
     * 获取配送方式
     *
     * @return
     */
    @PostMapping(value = "/express/template/list")
    @ResponseBody
    public ResponseResult getDeliveryExpressList(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestOrderConvert.convertDeliveryExpress(deliveryExpressTemplateService.getDeliveryExpressTemplateList()));
        return rr;
    }

    /**
     * 校验商品是否满足限购
     *
     * @param userId
     * @param spuId
     * @param count
     * @return
     */
    public Boolean checkProductLimitQty(Long userId, Long spuId, Integer count) {
        Boolean result = false;
        ProductSpuDO spuDO = productSpuService.getSpu(spuId);
        if (spuDO != null) {
            Integer limitCount = spuDO.getLimitCount();
            if (limitCount != null && limitCount.compareTo(0) > 0) {
                int payCount = tradeOrderQueryService.findPayProductCountByUserIdAndSpuId(userId, spuId);
                int balance = limitCount - payCount - count;
                if (balance < 0) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 结算前校验
     *
     * @return
     */
    @PostMapping(value = "/check_settlement")
    @ResponseBody
    public ResponseResult checkSettlement(HttpServletRequest request, String cartIds,
                                          Long skuId,Integer count,
                                          String exchangeCode, Long activityId) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        List<CartDO> carList = new ArrayList<>();
        if (skuId!=null){
            if (count==null){
                count=1;
            }

            if (count.compareTo(0)<=0){
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("购买数量不能小于等于0");
                return rr;
            }

            ProductSkuRespDTO skuDTO = productSkuApi.getSku(skuId);
            if (skuDTO==null){
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("找不到商品规格信息");
                return rr;
            }

            CartDO cartDO = new CartDO();
            cartDO.setSkuId(skuId);
            cartDO.setCount(count);
            carList.add(cartDO);
        }else{
            if (StringUtils.isEmpty(cartIds)) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("参数错误");
                return rr;
            }

            Set<Long> ids = new HashSet<>();
            for (String ss : cartIds.split(",")) {
                if (StringUtils.isNotEmpty(ss)) {
                    ids.add(Long.valueOf(ss));
                }
            }
            if (ids.isEmpty()) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("参数错误");
                return rr;
            }
            carList = cartService.getCartList(member.getId(), ids);
        }
        if (carList == null || carList.size() == 0) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }


        Map<Long, Integer> tmpMap = new HashMap<>();
        for (CartDO dd : carList) {
            if (tmpMap.containsKey(dd.getSpuId())) {
                tmpMap.put(dd.getSpuId(), tmpMap.get(dd.getSpuId()) + dd.getCount());
            } else {
                tmpMap.put(dd.getSpuId(), dd.getCount());
            }

        }
        for (Long spuId : tmpMap.keySet()) {
            if (checkProductLimitQty(member.getId(), spuId, tmpMap.get(spuId))) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("超过商品限购的最大数量");
                return rr;
            }
        }

        List<ProductSpuDO> spuList = productSpuService.getSpuList(CollectionUtils.convertSet(carList, CartDO::getSpuId));
        //判断购买商品是否在限时购买时间内
        if (spuList != null && spuList.size() > 0) {
            for (ProductSpuDO dd : spuList) {
                if (dd.getIsLimitTime() != null && dd.getIsLimitTime()) {
                    if (dd.getLimitTimeStart() != null) {
                        if (dd.getLimitTimeStart().isAfter(LocalDateTime.now())) {
                            rr.setCode(ResponseResult.ERROR);
                            rr.setMessage("[" + dd.getName() + "]还未到购买活动开始时间");
                            return rr;
                        }
                    }

                    if (dd.getLimitTimeEnd() != null) {
                        if (dd.getLimitTimeEnd().isBefore(LocalDateTime.now())) {
                            rr.setCode(ResponseResult.ERROR);
                            rr.setMessage("[" + dd.getName() + "]购买活动已结束");
                            return rr;
                        }
                    }
                }
            }
        }


        //判断产品中是否有首单优惠产品
        Boolean isFirstProduct = false;
        if (spuList != null && spuList.size() > 0) {
            for (ProductSpuDO dd : spuList) {
                if (dd.getIsFirstProduct() != null && dd.getIsFirstProduct()) {
                    isFirstProduct = true;
                    break;
                }
            }
        }
        if (isFirstProduct) {
            if (!tradeOrderQueryService.isNewUser(member.getId())) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("不能购买新人首单优惠产品");
                return rr;
            }
            int totalCount = 0;
            for (CartDO dd : carList) {
                totalCount = totalCount + dd.getCount();
            }
            if (totalCount > 1) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("新人首单优惠产品只能购买1个");
                return rr;
            }
        }

        //判断是否为特殊商品
        Boolean isSpecProduct = false;
        if (spuList != null && spuList.size() > 0) {
            for (ProductSpuDO dd : spuList) {
                if (isSpecSpu(dd.getCategoryId())) {
                    isSpecProduct = true;
                    break;
                }
            }
        }

        if (isSpecProduct) {
            if (carList.size() > 1) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("选择的商品有特殊商品，不能购买");
                return rr;
            }

            if (activityId != null) {
//                DrawActivityDO drawActivityDO = drawActivityService.getDrawActivity(activityId);
//                if (drawActivityDO == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("活动不存在或已下架");
//                    return rr;
//                }
//
//                if (drawActivityDO.getBuyEndTime() != null) {
//                    if (drawActivityDO.getBuyEndTime().isBefore(LocalDateTime.now())) {
//                        rr.setCode(ResponseResult.ERROR);
//                        rr.setMessage("活动已结束，不能购买");
//                        return rr;
//                    }
//                }
//
//                DrawActivityRecordDO recordDO = drawActivityRecordService.getWinRecord(member.getId(), activityId);
//                if (recordDO == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("找不到该活动中奖记录，不能购买");
//                    return rr;
//                }
//
//                if (recordDO.getResultId() == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("还未领取奖励，不能购买");
//                    return rr;
//                }
//
//                if (recordDO.getIsTake()) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该活动已领取奖品，不能重复领取");
//                    return rr;
//                }
            } else {
                if (StringUtils.isEmpty(exchangeCode)) {
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("该商品为特殊商品，需要兑换码");
                    return rr;
                }

//                ExchangeRecordDO exchangeRecord = exchangeRecordService.getUserCanUseExchangeRecord(member.getId(), exchangeCode);
//                if (exchangeRecord == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码无效");
//                    return rr;
//                }
//
//                if (!exchangeRecord.getBizType().equals(1) && !exchangeRecord.getBizType().equals(4)) {
//                    //商品兑换/周边产品兑换
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码不能用于商品兑换");
//                    return rr;
//                }

                CartDO cartDO = carList.get(0);
//                if (!cartDO.getSkuId().equals(exchangeRecord.getBizId())) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码不能用于该商品兑换");
//                    return rr;
//                }

                if (cartDO.getCount() > 1) {
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("兑换码只能兑换一个商品");
                    return rr;
                }

//                if (exchangeRecord.getValidStartTime() != null && exchangeRecord.getValidStartTime().isAfter(LocalDateTime.now())) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码还未生效");
//                    return rr;
//                }
//
//                if (exchangeRecord.getValidEndTime() != null && exchangeRecord.getValidEndTime().isBefore(LocalDateTime.now())) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码已过期");
//                    return rr;
//                }
            }
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 获得订单结算信息
     *
     * @return
     */
    @PostMapping(value = "/settlement")
    @ResponseBody
    public ResponseResult settlementOrder(HttpServletRequest request, String cartIds,
                                          Long skuId,Integer count,
                                          Long deliveryTemplateId, Long couponId, Long addressId,
                                          String exchangeCode, Long activityId) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        List<CartDO> carList = new ArrayList<>();
        if (skuId!=null){
            if (count==null){
                count=1;
            }

            if (count.compareTo(0)<=0){
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("购买数量不能小于等于0");
                return rr;
            }

            ProductSkuRespDTO skuDTO = productSkuApi.getSku(skuId);
            if (skuDTO==null){
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("找不到商品规格信息");
                return rr;
            }

            CartDO cartDO = new CartDO();
            cartDO.setSkuId(skuId);
            cartDO.setCount(count);
            carList.add(cartDO);
        }else{
            if (StringUtils.isEmpty(cartIds)) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("参数错误");
                return rr;
            }

            Set<Long> ids = new HashSet<>();
            for (String ss : cartIds.split(",")) {
                if (StringUtils.isNotEmpty(ss)) {
                    ids.add(Long.valueOf(ss));
                }
            }
            if (ids.isEmpty()) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("参数错误");
                return rr;
            }
            carList = cartService.getCartList(member.getId(), ids);
        }


        if (carList == null || carList.size() == 0) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        Map<Long, Integer> tmpMap = new HashMap<>();
        List<Long> spuIdList = new ArrayList<>();
        for (CartDO dd : carList) {
            if (tmpMap.containsKey(dd.getSpuId())) {
                tmpMap.put(dd.getSpuId(), tmpMap.get(dd.getSpuId()) + dd.getCount());
            } else {
                tmpMap.put(dd.getSpuId(), dd.getCount());
            }

            if (spuIdList.indexOf(dd.getSpuId()) == -1) {
                spuIdList.add(dd.getSpuId());
            }
        }
        //判断是否为特殊商品
        Boolean isSpecProduct = false;
        List<ProductSpuDO> spuList = productSpuService.getSpuList(spuIdList);
        if (spuList != null && spuList.size() > 0) {
            for (ProductSpuDO dd : spuList) {
                if (isSpecSpu(dd.getCategoryId())) {
                    isSpecProduct = true;
                    break;
                }
            }
        }

        if (isSpecProduct) {
            if (carList.size() > 1) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("选择的商品有特殊商品，不能购买");
                return rr;
            }

            if (activityId != null) {
//                DrawActivityDO drawActivityDO = drawActivityService.getDrawActivity(activityId);
//                if (drawActivityDO == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("活动不存在或已下架");
//                    return rr;
//                }
//
//                if (drawActivityDO.getBuyEndTime() != null) {
//                    if (drawActivityDO.getBuyEndTime().isBefore(LocalDateTime.now())) {
//                        rr.setCode(ResponseResult.ERROR);
//                        rr.setMessage("活动已结束，不能购买");
//                        return rr;
//                    }
//                }
//
//
//                DrawActivityRecordDO recordDO = drawActivityRecordService.getWinRecord(member.getId(), activityId);
//                if (recordDO == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("找不到该活动中奖记录，不能购买");
//                    return rr;
//                }
//
//                if (recordDO.getResultId() == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("还未领取奖励，不能购买");
//                    return rr;
//                }
//
//                if (recordDO.getIsTake()) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该活动已领取奖品，不能重复领取");
//                    return rr;
//                }
            } else {
                if (StringUtils.isEmpty(exchangeCode)) {
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("该商品为特殊商品，需要兑换码");
                    return rr;
                }

//                ExchangeRecordDO exchangeRecord = exchangeRecordService.getUserCanUseExchangeRecord(member.getId(), exchangeCode);
//                if (exchangeRecord == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码无效");
//                    return rr;
//                }
//
//                if (!exchangeRecord.getBizType().equals(1) && !exchangeRecord.getBizType().equals(4)) {
//                    //商品兑换/周边产品兑换
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码不能用于商品兑换");
//                    return rr;
//                }
//
//                CartDO cartDO = carList.get(0);
//                if (!cartDO.getSkuId().equals(exchangeRecord.getBizId())) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码不能用于该商品兑换");
//                    return rr;
//                }
//
//                if (cartDO.getCount() > 1) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("兑换码只能兑换一个商品");
//                    return rr;
//                }
//
//                if (exchangeRecord.getValidStartTime() != null && exchangeRecord.getValidStartTime().isAfter(LocalDateTime.now())) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码还未生效");
//                    return rr;
//                }
//
//                if (exchangeRecord.getValidEndTime() != null && exchangeRecord.getValidEndTime().isBefore(LocalDateTime.now())) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码已过期");
//                    return rr;
//                }
            }


        }

        for (Long spuId : tmpMap.keySet()) {
            if (checkProductLimitQty(member.getId(), spuId, tmpMap.get(spuId))) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("超过商品限购的最大数量");
                return rr;
            }
        }


        if (addressId == null) {
            MemberAddressRespDTO address = addressApi.getDefaultAddress(member.getId());
            if (address != null) {
                addressId = address.getId();
            }
        }

        try {
            AppTradeOrderSettlementReqVO settlementReqVO = new AppTradeOrderSettlementReqVO();
            settlementReqVO.setAddressId(addressId);
            settlementReqVO.setCouponId(couponId);
//            settlementReqVO.setDeliveryTemplateId(deliveryTemplateId);
            settlementReqVO.setPointStatus(false);
            settlementReqVO.setExchangeCode(exchangeCode);
            settlementReqVO.setFreeShipping(false);
            settlementReqVO.setActivityId(activityId);
            List<AppTradeOrderSettlementReqVO.Item> items = new ArrayList<>();
            for (CartDO dd : carList) {
                AppTradeOrderSettlementReqVO.Item item = new AppTradeOrderSettlementReqVO.Item();
                item.setCartId(dd.getId());
                item.setCount(dd.getCount());
                item.setSkuId(dd.getSkuId());
                items.add(item);
            }
            settlementReqVO.setItems(items);
            settlementReqVO.setDeliveryType(DeliveryTypeEnum.EXPRESS.getType());

            AppTradeOrderSettlementRespVO settlementRespVO = tradeOrderUpdateService.settlementOrder(member.getId(), settlementReqVO);
            //获取可用优惠劵列表
            if (settlementRespVO.getPrice().getPayPrice().equals(0) && settlementReqVO.getCouponId() == null) {
                settlementRespVO.setCoupons(new ArrayList<>());
            } else {
                settlementRespVO.setCoupons(BeanUtils.toBean(getCouponList(member.getId(), settlementRespVO), AppTradeOrderSettlementRespVO.Coupon.class));
            }

            //赠品信息
            ApiTradeOrderSettlementDO settlementDO = RestOrderConvert.convert(settlementRespVO);
            if (settlementDO != null) {
                List<ApiTradeOrderSettlementDO.Item> settlementItems = settlementDO.getItems();
                for (ApiTradeOrderSettlementDO.Item dd : settlementItems) {
                    ProductSpuDO spu = productSpuService.getSpu(dd.getSpuId());
                    if (spu != null && spu.getGiveGift() != null) {
                        ProductSpuDO gift = productSpuService.getSpu(spu.getGiveGift());
                        if (gift != null) {
                            dd.setGiftId(spu.getGiveGift());
                            dd.setGiftName(gift.getName());
                            dd.setGiftPicUrl(gift.getPicUrl());
                            dd.setGiftCount(dd.getCount());
                        }
                    }
                }
            }
            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            rr.setData(settlementDO);
            return rr;
        } catch (Exception e) {
            logger.error(e.getMessage());
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage(e.getMessage());
            return rr;
        }
    }

    /**
     * 创建订单
     *
     * @return
     */
    @PostMapping(value = "/create")
    @ResponseBody
    public ResponseResult createOrder(HttpServletRequest request, String cartIds,
                                      Long skuId,Integer count,
                                      Long deliveryTemplateId, Long couponId,
                                      Long addressId, String remark, String exchangeCode, Long activityId) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        List<CartDO> carList = new ArrayList<>();
        if (skuId!=null){
            if (count==null){
                count=1;
            }

            if (count.compareTo(0)<=0){
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("购买数量不能小于等于0");
                return rr;
            }

            ProductSkuRespDTO skuDTO = productSkuApi.getSku(skuId);
            if (skuDTO==null){
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("找不到商品规格信息");
                return rr;
            }

            CartDO cartDO = new CartDO();
            cartDO.setSkuId(skuId);
            cartDO.setCount(count);
            carList.add(cartDO);
        }else{
            if (StringUtils.isEmpty(cartIds)) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("参数错误");
                return rr;
            }

            Set<Long> ids = new HashSet<>();
            for (String ss : cartIds.split(",")) {
                if (StringUtils.isNotEmpty(ss)) {
                    ids.add(Long.valueOf(ss));
                }
            }
            if (ids.isEmpty()) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("参数错误");
                return rr;
            }
            carList = cartService.getCartList(member.getId(), ids);
        }

        if (carList == null || carList.size() == 0) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }


        if (addressId == null) {
            MemberAddressRespDTO address = addressApi.getDefaultAddress(member.getId());
            if (address == null) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("请先设置默认收货地址");
                return rr;
            }
            addressId = address.getId();
//            rr.setCode(ResponseResult.ERROR);
//            rr.setMessage("请选择收货地址");
//            return rr;
        }

//        if (deliveryTemplateId==null){
//            rr.setCode(ResponseResult.ERROR);
//            rr.setMessage("请选择配送方式");
//            return rr;
//        }



        List<ProductSpuDO> spuList = productSpuService.getSpuList(CollectionUtils.convertSet(carList, CartDO::getSpuId));
        //判断购买商品是否在限时购买时间内
        if (spuList != null && spuList.size() > 0) {
            for (ProductSpuDO dd : spuList) {
                if (dd.getIsLimitTime() != null && dd.getIsLimitTime()) {
                    if (dd.getLimitTimeStart() != null) {
                        if (dd.getLimitTimeStart().isAfter(LocalDateTime.now())) {
                            rr.setCode(ResponseResult.ERROR);
                            rr.setMessage("[" + dd.getName() + "]还未到购买活动开始时间");
                            return rr;
                        }
                    }

                    if (dd.getLimitTimeEnd() != null) {
                        if (dd.getLimitTimeEnd().isBefore(LocalDateTime.now())) {
                            rr.setCode(ResponseResult.ERROR);
                            rr.setMessage("[" + dd.getName() + "]购买活动已结束");
                            return rr;
                        }
                    }
                }
            }
        }

        //判断产品中是否有首单优惠产品
        Boolean isFirstProduct = false;
        if (spuList != null && spuList.size() > 0) {
            for (ProductSpuDO dd : spuList) {
                if (dd.getIsFirstProduct() != null && dd.getIsFirstProduct()) {
                    isFirstProduct = true;
                    break;
                }
            }
        }
        if (isFirstProduct) {
            if (!tradeOrderQueryService.isNewUser(member.getId())) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("不能购买新人首单优惠产品");
                return rr;
            }
            int totalCount = 0;
            for (CartDO dd : carList) {
                totalCount = totalCount + dd.getCount();
            }
            if (totalCount > 1) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("新人首单优惠产品只能购买1个");
                return rr;
            }
        }

        //判断是否为特殊商品
        Boolean isSpecProduct = false;
        if (spuList != null && spuList.size() > 0) {
            for (ProductSpuDO dd : spuList) {
                if (isSpecSpu(dd.getCategoryId())) {
                    isSpecProduct = true;
                    break;
                }
            }
        }

        if (isSpecProduct) {
            if (carList.size() > 1) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("选择的商品有特殊商品，不能购买");
                return rr;
            }

            if (activityId != null) {
//                DrawActivityDO drawActivityDO = drawActivityService.getDrawActivity(activityId);
//                if (drawActivityDO == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("活动不存在或已下架");
//                    return rr;
//                }
//
//                if (drawActivityDO.getBuyEndTime() != null) {
//                    if (drawActivityDO.getBuyEndTime().isBefore(LocalDateTime.now())) {
//                        rr.setCode(ResponseResult.ERROR);
//                        rr.setMessage("活动已结束，不能购买");
//                        return rr;
//                    }
//                }
//
//
//                DrawActivityRecordDO recordDO = drawActivityRecordService.getWinRecord(member.getId(), activityId);
//                if (recordDO == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("找不到该活动中奖记录，不能购买");
//                    return rr;
//                }
//
//                if (recordDO.getResultId() == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("还未领取奖励，不能购买");
//                    return rr;
//                }
//
//                if (recordDO.getIsTake()) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该活动已领取奖品，不能重复领取");
//                    return rr;
//                }
            } else {
                if (StringUtils.isEmpty(exchangeCode)) {
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("该商品为特殊商品，需要兑换码");
                    return rr;
                }

//                ExchangeRecordDO exchangeRecord = exchangeRecordService.getUserCanUseExchangeRecord(member.getId(), exchangeCode);
//                if (exchangeRecord == null) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码无效");
//                    return rr;
//                }
//
//                if (!exchangeRecord.getBizType().equals(1) && !exchangeRecord.getBizType().equals(4)) {
//                    //商品兑换/周边产品兑换
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码不能用于商品兑换");
//                    return rr;
//                }

                CartDO cartDO = carList.get(0);
//                if (!cartDO.getSkuId().equals(exchangeRecord.getBizId())) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码不能用于该商品兑换");
//                    return rr;
//                }

                if (cartDO.getCount() > 1) {
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("兑换码只能兑换一个商品");
                    return rr;
                }

//                if (exchangeRecord.getValidStartTime() != null && exchangeRecord.getValidStartTime().isAfter(LocalDateTime.now())) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码还未生效");
//                    return rr;
//                }
//
//                if (exchangeRecord.getValidEndTime() != null && exchangeRecord.getValidEndTime().isBefore(LocalDateTime.now())) {
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("该兑换码已过期");
//                    return rr;
//                }
            }
        }

        Map<Long, Integer> tmpMap = new HashMap<>();
        for (CartDO dd : carList) {
            if (tmpMap.containsKey(dd.getSpuId())) {
                tmpMap.put(dd.getSpuId(), tmpMap.get(dd.getSpuId()) + dd.getCount());
            } else {
                tmpMap.put(dd.getSpuId(), dd.getCount());
            }

        }
        for (Long spuId : tmpMap.keySet()) {
            if (checkProductLimitQty(member.getId(), spuId, tmpMap.get(spuId))) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("超过商品限购的最大数量");
                return rr;
            }
        }

        try {
            AppTradeOrderCreateReqVO createReqVO = new AppTradeOrderCreateReqVO();
            createReqVO.setAddressId(addressId);
            createReqVO.setCouponId(couponId);
//            createReqVO.setDeliveryTemplateId(deliveryTemplateId);
            createReqVO.setPointStatus(false);
            createReqVO.setExchangeCode(exchangeCode);
            createReqVO.setFreeShipping(false);
            createReqVO.setActivityId(activityId);
            List<AppTradeOrderSettlementReqVO.Item> items = new ArrayList<>();
            for (CartDO dd : carList) {
                AppTradeOrderSettlementReqVO.Item item = new AppTradeOrderSettlementReqVO.Item();
                item.setCartId(dd.getId());
                item.setCount(dd.getCount());
                item.setSkuId(dd.getSkuId());
                items.add(item);
            }
            createReqVO.setItems(items);
            createReqVO.setDeliveryType(DeliveryTypeEnum.EXPRESS.getType());
            createReqVO.setRemark(remark);
            TradeOrderDO order = tradeOrderUpdateService.createOrder(member.getId(), getClientIP(), createReqVO, TerminalEnum.WECHAT_MINI_PROGRAM.getTerminal());
            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            rr.setData(new AppTradeOrderCreateRespVO().setId(order.getId()).setPayOrderId(order.getPayOrderId()).setPayStatus(order.getPayStatus()));
            return rr;
        } catch (Exception e) {
            logger.error(e.getMessage());
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage(e.getMessage());
            return rr;
        }
    }

    /**
     * 获取可用优惠劵列表
     *
     * @param userId
     * @param settlementRespVO
     * @return
     */
    public List<AppCouponMatchRespVO> getCouponList(Long userId, AppTradeOrderSettlementRespVO settlementRespVO) {
        List<AppCouponMatchRespVO> resultList = new ArrayList<>();
        if (settlementRespVO != null && settlementRespVO.getItems().size() > 0) {
            AppCouponMatchReqVO matchReqVO = new AppCouponMatchReqVO();
            matchReqVO.setPrice(settlementRespVO.getPrice().getTotalPrice());
            List<Long> spuIds = new ArrayList<>();
            List<Long> skuIds = new ArrayList<>();
            List<Long> categoryIds = new ArrayList<>();
            if (settlementRespVO.getItems() != null) {
                for (AppTradeOrderSettlementRespVO.Item dd : settlementRespVO.getItems()) {
                    if (spuIds.indexOf(dd.getSpuId()) == -1) {
                        spuIds.add(dd.getSpuId());
                    }
                    if (skuIds.indexOf(dd.getSkuId()) == -1) {
                        skuIds.add(Long.valueOf(dd.getSkuId()));
                    }
                    if (categoryIds.indexOf(dd.getCategoryId()) == -1) {
                        categoryIds.add(dd.getCategoryId());
                    }
                }
            }
            matchReqVO.setSpuIds(spuIds);
            matchReqVO.setSkuIds(skuIds);
            matchReqVO.setCategoryIds(categoryIds);
            resultList = CouponConvert.INSTANCE.convertList(couponService.getMatchCouponList(userId, matchReqVO));

            //如果存在包邮商品(或者不是发货单)，则过滤包邮卡优惠劵
            Boolean isFilterFreeCard = false;
            if (settlementRespVO.getFreeShipping() != null && settlementRespVO.getFreeShipping()) {
                isFilterFreeCard = true;
            }
            if (isFilterFreeCard) {
                if (resultList.size() > 0) {
                    List<AppCouponMatchRespVO> tmpList = new ArrayList<>();
                    for (AppCouponMatchRespVO dd : resultList) {
                        if (!dd.getName().contains("包邮") && !dd.getName().contains("邮费")) {
                            tmpList.add(dd);
                        }
                    }

                    resultList.clear();
                    resultList.addAll(tmpList);
                }
            }
        }

        if (resultList == null) {
            resultList = new ArrayList<>();
        }
        return resultList;
    }


    /**
     * 发起支付
     *
     * @return
     */
    @PostMapping(value = "/pay/submit")
    @ResponseBody
    public ResponseResult submitPayOrder(HttpServletRequest request, Long id, String returnUrl, String channelCode) {
        ResponseResult rr = new ResponseResult();
        SocialUserDO socialUser = getCurrentSocialUser(request);
        if (socialUser == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        MemberUserDO member = getMemberUser(socialUser.getId());
        if (member == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("会员不存在");
            return rr;
        }

        if (id == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        if (StringUtils.isEmpty(channelCode)) {
            channelCode = "wx_lite";
        }

        PayOrderDO payOrderDO = payOrderService.getOrder(id);
        if (payOrderDO == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("支付单不存在");
            return rr;
        }

        TradeOrderDO orderDO = tradeOrderQueryService.getOrder(Long.valueOf(payOrderDO.getMerchantOrderId()));
        if (orderDO == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("订单不存在");
            return rr;
        }

        if (orderDO.getType().equals(TradeOrderTypeEnum.NORMAL.getType())) {
            List<TradeOrderItemDO> orderItemList = tradeOrderQueryService.getOrderItemListByOrderId(Long.valueOf(payOrderDO.getMerchantOrderId()));
            Boolean isFirstProduct = false;

            List<ProductSpuDO> spuList = productSpuService.getSpuList(CollectionUtils.convertSet(orderItemList, TradeOrderItemDO::getSpuId));
            if (spuList != null && spuList.size() > 0) {
                for (ProductSpuDO dd : spuList) {
                    if (dd.getIsFirstProduct() != null && dd.getIsFirstProduct()) {
                        isFirstProduct = true;
                        break;
                    }
                }
            }

            if (isFirstProduct) {
                if (!tradeOrderQueryService.isNewUser(member.getId())) {
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("不能购买新人首单优惠产品");
                    return rr;
                }
                int totalCount = 0;
                for (TradeOrderItemDO dd : orderItemList) {
                    totalCount = totalCount + dd.getCount();
                }
                if (totalCount > 1) {
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("新人首单优惠产品只能购买1个");
                    return rr;
                }
            }
        }

//        if (orderDO.getNo().equalsIgnoreCase("o202404121426461")){
//            logger.info("跟踪订单o202404121426461 2");
//        }

        AppPayOrderSubmitReqVO reqVO = new AppPayOrderSubmitReqVO();
        reqVO.setId(id);
        reqVO.setChannelCode(channelCode);
        reqVO.setReturnUrl(returnUrl);
        Map<String, String> channelExtras = new HashMap<>();
        channelExtras.put("openid", socialUser.getOpenid());
        reqVO.setChannelExtras(channelExtras);

        //提交支付
        PayOrderSubmitRespVO respVO = payOrderService.submitOrder(reqVO, getClientIP());
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(PayOrderConvert.INSTANCE.convert3(respVO));
        return rr;
    }

    /**
     * 查询支付订单
     *
     * @return
     */
    @PostMapping(value = "/pay/get")
    @ResponseBody
    public ResponseResult getPayOrder(HttpServletRequest request, Long id) {
        ResponseResult rr = new ResponseResult();
        SocialUserDO socialUser = getCurrentSocialUser(request);
        if (socialUser == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (id == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestOrderConvert.convert(PayOrderConvert.INSTANCE.convert(payOrderService.getOrder(id))));
        return rr;
    }

    /**
     * 更新订单为已支付
     *
     * @return
     */
    @PostMapping("/update-paid")
    @ResponseBody
    public ResponseResult updateOrderPaid(HttpServletRequest request, PayOrderNotifyReqDTO notifyReqDTO) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        if (notifyReqDTO.getMerchantOrderId() == null || notifyReqDTO.getPayOrderId() == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        tradeOrderUpdateService.updateOrderPaid(Long.valueOf(notifyReqDTO.getMerchantOrderId()),
                notifyReqDTO.getPayOrderId());
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 获得交易订单
     *
     * @return
     */
    @RequestMapping("/get-detail")
    @ResponseBody
    public ResponseResult getOrder(HttpServletRequest request, Long id,String payOrderNo) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (id==null && StringUtils.isEmpty(payOrderNo)){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }
        // 查询订单
        TradeOrderDO order =null;
        if (id!=null){
            order = tradeOrderQueryService.getOrder(member.getId(), id);
        }else{
            PayOrderExtensionDO extensionDO = payOrderService.getOrderExtensionByNo(payOrderNo);
            if (extensionDO!=null){
                Long orderId = extensionDO.getOrderId();
                order = tradeOrderQueryService.getOrder(member.getId(), orderId);
            }
        }
        if (order == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("订单不存在");
            return rr;
        }
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(order.getId());
        // 查询物流公司
        DeliveryExpressDO express = order.getLogisticsId() != null && order.getLogisticsId() > 0 ?
                deliveryExpressService.getDeliveryExpress(order.getLogisticsId()) : null;
        ApiTradeOrderDetailDO detailDO = RestOrderConvert.convert(TradeOrderConvert.INSTANCE.convert02(order, orderItems, tradeOrderProperties, express));
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(detailDO);
        return rr;
    }

    /**
     * 获得交易订单的物流轨迹
     *
     * @return
     */
    @RequestMapping("/get-express-track-list")
    @ResponseBody
    public ResponseResult getOrderExpressTrackList(HttpServletRequest request, Long id) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (id == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestOrderConvert.convert(TradeOrderConvert.INSTANCE.convertList02(
                tradeOrderQueryService.getExpressTrackList(id, member.getId()))));
        return rr;
    }

    /**
     * 获得交易订单分页
     *
     * @return
     */
    @RequestMapping("/page")
    @ResponseBody
    public ResponseResult getOrderPage(HttpServletRequest request, AppTradeOrderPageReqVO reqVO) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        // 查询订单
        PageResult<TradeOrderDO> pageResult = tradeOrderQueryService.getOrderPage(member.getId(), reqVO);
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                convertSet(pageResult.getList(), TradeOrderDO::getId));

        PageResult<ApiTradeOrderDetailDO> page = RestOrderConvert.convertPage(TradeOrderConvert.INSTANCE.convertPage02(pageResult, orderItems));
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(page);
        return rr;
    }

    /**
     * 获得交易订单数量
     *
     * @return
     */
    @RequestMapping("/get-count")
    @ResponseBody
    public ResponseResult getOrderCount(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        Map<String, Long> orderCount = Maps.newLinkedHashMapWithExpectedSize(5);
        // 全部
        AppTradeOrderPageReqVO reqVO = new AppTradeOrderPageReqVO();
        orderCount.put("allCount", tradeOrderQueryService.getOrderCount(member.getId(), reqVO));
        // 待付款（未支付）
        reqVO = new AppTradeOrderPageReqVO();
        reqVO.setStatus(TradeOrderStatusEnum.UNPAID.getStatus());
        orderCount.put("unpaidCount", tradeOrderQueryService.getOrderCount(member.getId(), reqVO));
        // 待发货
        reqVO = new AppTradeOrderPageReqVO();
        reqVO.setStatus(TradeOrderStatusEnum.UNDELIVERED.getStatus());
        orderCount.put("undeliveredCount", tradeOrderQueryService.getOrderCount(member.getId(), reqVO));
        // 待收货
        reqVO = new AppTradeOrderPageReqVO();
        reqVO.setStatus(TradeOrderStatusEnum.DELIVERED.getStatus());
        orderCount.put("deliveredCount", tradeOrderQueryService.getOrderCount(member.getId(), reqVO));
        // 已完成
        reqVO = new AppTradeOrderPageReqVO();
        reqVO.setStatus(TradeOrderStatusEnum.COMPLETED.getStatus());
        orderCount.put("uncommentedCount", tradeOrderQueryService.getOrderCount(member.getId(), reqVO));

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(orderCount);
        return rr;
    }

    /**
     * 确认交易订单收货
     *
     * @return
     */
    @PostMapping("/receive")
    @ResponseBody
    public ResponseResult receiveOrder(HttpServletRequest request, Long id) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        if (id == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        tradeOrderUpdateService.receiveOrderByMember(member.getId(), id);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 取消交易订单
     *
     * @return
     */
    @PostMapping("/cancel")
    @ResponseBody
    public ResponseResult cancelOrder(HttpServletRequest request, Long id) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        if (id == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        tradeOrderUpdateService.cancelOrderByMember(member.getId(), id);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 获得交易订单项
     *
     * @return
     */
    @RequestMapping("/item/get")
    @ResponseBody
    public ResponseResult getOrderItem(HttpServletRequest request, Long id) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        if (id == null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        TradeOrderItemDO item = tradeOrderQueryService.getOrderItem(member.getId(), id);
        if (item != null) {
            TradeOrderDO order = tradeOrderQueryService.getOrder(member.getId(), item.getOrderId());
            if (order == null) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("订单不存在");
                return rr;
            }
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestOrderConvert.convert(item));
        return rr;
    }


    /**
     * 判断是否为特殊商品
     *
     * @return
     */
    public Boolean isSpecSpu(Long categoryId) {
        Boolean result = false;
        if (categoryId != null) {
            List<Long> parentIdList = new ArrayList<>();
            parentIdList.add(CATEGORY_SPEC_ID);
            List<ProductCategoryDO> categoryChildren = categoryService.getEnableCategoryList(parentIdList);
            if (categoryChildren != null) {
                for (ProductCategoryDO dd : categoryChildren) {
                    if (dd.getId().equals(categoryId)) {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }


}
