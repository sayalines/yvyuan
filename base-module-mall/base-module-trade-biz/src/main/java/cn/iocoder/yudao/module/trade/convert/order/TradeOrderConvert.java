package cn.iocoder.yudao.module.trade.convert.order;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import cn.iocoder.yudao.framework.dict.core.util.DictFrameworkUtils;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.module.member.api.address.dto.MemberAddressRespDTO;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderCreateReqDTO;
import cn.iocoder.yudao.module.pay.enums.DictTypeConstants;
import cn.iocoder.yudao.module.product.api.comment.dto.ProductCommentCreateReqDTO;
import cn.iocoder.yudao.module.product.api.property.dto.ProductPropertyValueDetailRespDTO;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuUpdateStockReqDTO;
import cn.iocoder.yudao.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.yudao.module.promotion.api.combination.dto.CombinationRecordCreateReqDTO;
import cn.iocoder.yudao.module.trade.api.order.dto.TradeOrderRespDTO;
import cn.iocoder.yudao.module.trade.controller.admin.base.member.user.MemberUserRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.base.product.property.ProductPropertyValueDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.*;
import cn.iocoder.yudao.module.trade.controller.app.base.property.AppProductPropertyValueDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.*;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.item.AppTradeOrderItemCommentCreateReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.item.AppTradeOrderItemRespVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.cart.CartDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.delivery.DeliveryExpressDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderLogDO;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderItemAfterSaleStatusEnum;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.ExpressTrackRespDTO;
import cn.iocoder.yudao.module.trade.framework.order.config.TradeOrderProperties;
import cn.iocoder.yudao.module.trade.service.brokerage.bo.BrokerageAddReqBO;
import cn.iocoder.yudao.module.trade.service.price.bo.TradePriceCalculateReqBO;
import cn.iocoder.yudao.module.trade.service.price.bo.TradePriceCalculateRespBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMultiMap;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.addTime;

@Mapper
public interface TradeOrderConvert {

    TradeOrderConvert INSTANCE = Mappers.getMapper(TradeOrderConvert.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "userId", target = "userId"),
            @Mapping(source = "createReqVO.couponId", target = "couponId"),
            @Mapping(source = "createReqVO.exchangeCode", target = "exchangeCode"),
            @Mapping(source = "createReqVO.activityId", target = "activityId"),
            @Mapping(target = "remark", ignore = true),
            @Mapping(source = "createReqVO.remark", target = "userRemark"),
            @Mapping(source = "calculateRespBO.price.totalPrice", target = "totalPrice"),
            @Mapping(source = "calculateRespBO.price.discountPrice", target = "discountPrice"),
            @Mapping(source = "calculateRespBO.price.deliveryPrice", target = "deliveryPrice"),
            @Mapping(source = "calculateRespBO.price.couponPrice", target = "couponPrice"),
            @Mapping(source = "calculateRespBO.price.pointPrice", target = "pointPrice"),
            @Mapping(source = "calculateRespBO.price.vipPrice", target = "vipPrice"),
            @Mapping(source = "calculateRespBO.price.payPrice", target = "payPrice")
    })
    TradeOrderDO convert(Long userId, String userIp, AppTradeOrderCreateReqVO createReqVO, TradePriceCalculateRespBO calculateRespBO);

    TradeOrderRespDTO convert(TradeOrderDO orderDO);

    default List<TradeOrderItemDO> convertList(TradeOrderDO tradeOrderDO, TradePriceCalculateRespBO calculateRespBO) {
        return CollectionUtils.convertList(calculateRespBO.getItems(), item -> {
            TradeOrderItemDO orderItem = convert(item);
            orderItem.setOrderId(tradeOrderDO.getId());
            orderItem.setUserId(tradeOrderDO.getUserId());
            orderItem.setAfterSaleStatus(TradeOrderItemAfterSaleStatusEnum.NONE.getStatus());
            orderItem.setCommentStatus(false);
            return orderItem;
        });
    }

    TradeOrderItemDO convert(TradePriceCalculateRespBO.OrderItem item);

    default ProductSkuUpdateStockReqDTO convert(List<TradeOrderItemDO> list) {
        List<ProductSkuUpdateStockReqDTO.Item> items = CollectionUtils.convertList(list, item ->
                new ProductSkuUpdateStockReqDTO.Item().setId(item.getSkuId()).setIncrCount(item.getCount()));
        return new ProductSkuUpdateStockReqDTO(items);
    }

    default ProductSkuUpdateStockReqDTO convertNegative(List<TradeOrderItemDO> list) {
        List<ProductSkuUpdateStockReqDTO.Item> items = CollectionUtils.convertList(list, item ->
                new ProductSkuUpdateStockReqDTO.Item().setId(item.getSkuId()).setIncrCount(-item.getCount()));
        return new ProductSkuUpdateStockReqDTO(items);
    }

    default PayOrderCreateReqDTO convert(TradeOrderDO order, List<TradeOrderItemDO> orderItems,
                                         TradeOrderProperties orderProperties) {
        PayOrderCreateReqDTO createReqDTO = new PayOrderCreateReqDTO()
                .setAppId(orderProperties.getAppId()).setUserIp(order.getUserIp());
        // 商户相关字段
        createReqDTO.setMerchantOrderId(String.valueOf(order.getId()));
        String subject = orderItems.get(0).getSpuName();
        subject = StrUtils.maxLength(subject, PayOrderCreateReqDTO.SUBJECT_MAX_LENGTH); // 避免超过 32 位
        createReqDTO.setSubject(subject);
        createReqDTO.setBody(subject); // TODO 芋艿：临时写死
        // 订单相关字段
        createReqDTO.setPrice(order.getPayPrice()).setExpireTime(addTime(orderProperties.getPayExpireTime()));
        return createReqDTO;
    }

    default PageResult<TradeOrderPageItemRespVO> convertPage(PageResult<TradeOrderDO> pageResult,
                                                             List<TradeOrderItemDO> orderItems,
                                                             Map<Long, MemberUserRespDTO> memberUserMap) {
        Map<Long, List<TradeOrderItemDO>> orderItemMap = convertMultiMap(orderItems, TradeOrderItemDO::getOrderId);
        // 转化 List
        List<TradeOrderPageItemRespVO> orderVOs = CollectionUtils.convertList(pageResult.getList(), order -> {
            List<TradeOrderItemDO> xOrderItems = orderItemMap.get(order.getId());
            TradeOrderPageItemRespVO orderVO = convert(order, xOrderItems);
            // 处理收货地址
            orderVO.setReceiverAreaName(AreaUtils.format(order.getReceiverAreaId()));
            // 增加用户信息
            orderVO.setUser(convertUser(memberUserMap.get(orderVO.getUserId())));
            // 增加推广人信息
            orderVO.setBrokerageUser(convertUser(memberUserMap.get(orderVO.getBrokerageUserId())));
            return orderVO;
        });
        return new PageResult<>(orderVOs, pageResult.getTotal());
    }

    MemberUserRespVO convertUser(MemberUserRespDTO memberUserRespDTO);

    TradeOrderPageItemRespVO convert(TradeOrderDO order, List<TradeOrderItemDO> items);

    ProductPropertyValueDetailRespVO convert(ProductPropertyValueDetailRespDTO bean);

    default TradeOrderDetailRespVO convert(TradeOrderDO order, List<TradeOrderItemDO> orderItems,
                                           List<TradeOrderLogDO> orderLogs,
                                           MemberUserRespDTO user, MemberUserRespDTO brokerageUser) {
        TradeOrderDetailRespVO orderVO = convert20(order, orderItems);
        // 处理收货地址
        orderVO.setReceiverAreaName(AreaUtils.format(order.getReceiverAreaId()));
        // 处理用户信息
        orderVO.setUser(convert(user));
        orderVO.setBrokerageUser(convert(brokerageUser));
        // 处理日志
        orderVO.setLogs(convertList03(orderLogs));
        return orderVO;
    }

    List<TradeOrderDetailRespVO.OrderLog> convertList03(List<TradeOrderLogDO> orderLogs);

    TradeOrderDetailRespVO convert2(TradeOrderDO order, List<TradeOrderItemDO> items);

    default TradeOrderDetailRespVO convert20(TradeOrderDO order, List<TradeOrderItemDO> items) {
        TradeOrderDetailRespVO respVO = convert2(order, items);
        respVO.setGivePoint(order.getGivePoint());
        respVO.setExchangeCode(order.getExchangeCode());
        return respVO;
    }

    ;

    MemberUserRespVO convert(MemberUserRespDTO bean);

    default PageResult<AppTradeOrderPageItemRespVO> convertPage02(PageResult<TradeOrderDO> pageResult,
                                                                  List<TradeOrderItemDO> orderItems) {
        Map<Long, List<TradeOrderItemDO>> orderItemMap = convertMultiMap(orderItems, TradeOrderItemDO::getOrderId);
        // 转化 List
        List<AppTradeOrderPageItemRespVO> orderVOs = CollectionUtils.convertList(pageResult.getList(), order -> {
            List<TradeOrderItemDO> xOrderItems = orderItemMap.get(order.getId());
            AppTradeOrderPageItemRespVO vo = convert100(order, xOrderItems);
            vo.setPayStatus(order.getPayStatus());
            return vo;
        });
        return new PageResult<>(orderVOs, pageResult.getTotal());
    }

    AppTradeOrderPageItemRespVO convert02(TradeOrderDO order, List<TradeOrderItemDO> items);

    AppProductPropertyValueDetailRespVO convert02(ProductPropertyValueDetailRespDTO bean);

    default AppProductPropertyValueDetailRespVO convert010(ProductPropertyValueDetailRespDTO bean) {
        if (bean == null) {
            return null;
        }

        AppProductPropertyValueDetailRespVO appProductPropertyValueDetailRespVO = new AppProductPropertyValueDetailRespVO();

        appProductPropertyValueDetailRespVO.setPropertyId(bean.getPropertyId());
        appProductPropertyValueDetailRespVO.setPropertyName(bean.getPropertyName());
        appProductPropertyValueDetailRespVO.setValueId(bean.getValueId());
        appProductPropertyValueDetailRespVO.setValueName(bean.getValueName());

        return appProductPropertyValueDetailRespVO;
    }

    default AppTradeOrderDetailRespVO convert02(TradeOrderDO order, List<TradeOrderItemDO> orderItems,
                                                TradeOrderProperties tradeOrderProperties,
                                                DeliveryExpressDO express) {
        AppTradeOrderDetailRespVO orderVO = convert10(order, orderItems);
        orderVO.setDeliveryTemplateId(order.getDeliveryTemplateId());
        orderVO.setGivePoint(order.getGivePoint());
        orderVO.setPayExpireTime(addTime(tradeOrderProperties.getPayExpireTime()));

        if (StrUtil.isNotEmpty(order.getPayChannelCode())) {
            orderVO.setPayChannelName(DictFrameworkUtils.getDictDataLabel(DictTypeConstants.CHANNEL_CODE, order.getPayChannelCode()));
        }
        // 处理收货地址
        orderVO.setReceiverAreaName(AreaUtils.format(order.getReceiverAreaId()));
        if (express != null) {
            orderVO.setLogisticsId(express.getId()).setLogisticsName(express.getName());
        }
        return orderVO;
    }

    AppTradeOrderDetailRespVO convert3(TradeOrderDO order, List<TradeOrderItemDO> items);

    AppTradeOrderItemRespVO convert03(TradeOrderItemDO bean);

    @Mappings({
            @Mapping(target = "skuId", source = "tradeOrderItemDO.skuId"),
            @Mapping(target = "orderId", source = "tradeOrderItemDO.orderId"),
            @Mapping(target = "orderItemId", source = "tradeOrderItemDO.id"),
            @Mapping(target = "descriptionScores", source = "createReqVO.descriptionScores"),
            @Mapping(target = "benefitScores", source = "createReqVO.benefitScores"),
            @Mapping(target = "content", source = "createReqVO.content"),
            @Mapping(target = "picUrls", source = "createReqVO.picUrls"),
            @Mapping(target = "anonymous", source = "createReqVO.anonymous"),
            @Mapping(target = "userId", source = "tradeOrderItemDO.userId")
    })
    ProductCommentCreateReqDTO convert04(AppTradeOrderItemCommentCreateReqVO createReqVO, TradeOrderItemDO tradeOrderItemDO);

    TradePriceCalculateReqBO convert(AppTradeOrderSettlementReqVO settlementReqVO);

    default TradePriceCalculateReqBO convert(Long userId, AppTradeOrderSettlementReqVO settlementReqVO,
                                             List<CartDO> cartList) {
        TradePriceCalculateReqBO reqBO = new TradePriceCalculateReqBO().setUserId(userId)
                .setItems(new ArrayList<>(settlementReqVO.getItems().size()))
                .setCouponId(settlementReqVO.getCouponId()).setPointStatus(settlementReqVO.getPointStatus())
                // 物流信息
                .setDeliveryType(settlementReqVO.getDeliveryType()).setDeliveryTemplateId(settlementReqVO.getDeliveryTemplateId())
                .setAddressId(settlementReqVO.getAddressId())
                .setPickUpStoreId(settlementReqVO.getPickUpStoreId())
                // 各种活动
                .setSeckillActivityId(settlementReqVO.getSeckillActivityId())
                .setBargainRecordId(settlementReqVO.getBargainRecordId())
                .setCombinationActivityId(settlementReqVO.getCombinationActivityId())
                .setCombinationHeadId(settlementReqVO.getCombinationHeadId())
                .setExchangeCode(settlementReqVO.getExchangeCode())
                .setFreeShipping(settlementReqVO.getFreeShipping())
                .setActivityId(settlementReqVO.getActivityId());

        // 商品项的构建
        Map<Long, CartDO> cartMap = new HashMap<>();
        if (cartList != null && cartList.size() > 0) {
            cartMap = convertMap(cartList, CartDO::getId);
        }
        for (AppTradeOrderSettlementReqVO.Item item : settlementReqVO.getItems()) {
            // 情况一：skuId + count
            if (item.getSkuId() != null) {
                reqBO.getItems().add(new TradePriceCalculateReqBO.Item().setSkuId(item.getSkuId()).setCount(item.getCount())
                        .setIsGift(item.getIsGift())
                        .setSelected(true)); // true 的原因，下单一定选中
                continue;
            }
            // 情况二：cartId
            CartDO cart = cartMap.get(item.getCartId());
            if (cart == null) {
                continue;
            }
            reqBO.getItems().add(new TradePriceCalculateReqBO.Item().setSkuId(cart.getSkuId()).setCount(cart.getCount())
                    .setCartId(item.getCartId()).setSelected(true)); // true 的原因，下单一定选中
        }
        return reqBO;
    }

    default AppTradeOrderSettlementRespVO convert(TradePriceCalculateRespBO calculate, MemberAddressRespDTO address) {
        AppTradeOrderSettlementRespVO respVO = convert10(calculate, address);
        if (address != null) {
            respVO.getAddress().setAreaName(AreaUtils.format(address.getAreaId()));
        }
        return respVO;
    }

    AppTradeOrderSettlementRespVO convert0(TradePriceCalculateRespBO calculate, MemberAddressRespDTO address);

    default AppTradeOrderSettlementRespVO convert10(TradePriceCalculateRespBO calculate, MemberAddressRespDTO address) {
        if (calculate == null && address == null) {
            return null;
        }

        AppTradeOrderSettlementRespVO appTradeOrderSettlementRespVO = new AppTradeOrderSettlementRespVO();

        if (calculate != null) {
            appTradeOrderSettlementRespVO.setType(calculate.getType());
            appTradeOrderSettlementRespVO.setItems(orderItemListToItemList10(calculate.getItems()));
            appTradeOrderSettlementRespVO.setPrice(priceToPrice10(calculate.getPrice()));
            appTradeOrderSettlementRespVO.setGivePoint(calculate.getGivePoint());
            appTradeOrderSettlementRespVO.setExchangeCode(calculate.getExchangeCode());
        }
        appTradeOrderSettlementRespVO.setAddress(memberAddressRespDTOToAddress10(address));

        return appTradeOrderSettlementRespVO;
    }

    default List<AppTradeOrderSettlementRespVO.Item> orderItemListToItemList10(List<TradePriceCalculateRespBO.OrderItem> list) {
        if (list == null) {
            return null;
        }

        List<AppTradeOrderSettlementRespVO.Item> list1 = new ArrayList<AppTradeOrderSettlementRespVO.Item>(list.size());
        for (TradePriceCalculateRespBO.OrderItem orderItem : list) {
            list1.add(orderItemToItem10(orderItem));
        }

        return list1;
    }

    default AppTradeOrderSettlementRespVO.Item orderItemToItem10(TradePriceCalculateRespBO.OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        AppTradeOrderSettlementRespVO.Item item = new AppTradeOrderSettlementRespVO.Item();

        item.setCategoryId(orderItem.getCategoryId());
        item.setSpuId(orderItem.getSpuId());
        item.setSpuName(orderItem.getSpuName());
        if (orderItem.getSkuId() != null) {
            item.setSkuId(orderItem.getSkuId().intValue());
        }
        item.setPrice(orderItem.getPrice());
        item.setPicUrl(orderItem.getPicUrl());
        item.setProperties(productPropertyValueDetailRespDTOListToAppProductPropertyValueDetailRespVOList10(orderItem.getProperties()));
        item.setCartId(orderItem.getCartId());
        item.setCount(orderItem.getCount());

        return item;
    }

    default List<AppProductPropertyValueDetailRespVO> productPropertyValueDetailRespDTOListToAppProductPropertyValueDetailRespVOList10(List<ProductPropertyValueDetailRespDTO> list) {
        if (list == null) {
            return null;
        }

        List<AppProductPropertyValueDetailRespVO> list1 = new ArrayList<AppProductPropertyValueDetailRespVO>(list.size());
        for (ProductPropertyValueDetailRespDTO productPropertyValueDetailRespDTO : list) {
            list1.add(convert010(productPropertyValueDetailRespDTO));
        }

        return list1;
    }

    default AppTradeOrderSettlementRespVO.Address memberAddressRespDTOToAddress10(MemberAddressRespDTO memberAddressRespDTO) {
        if (memberAddressRespDTO == null) {
            return null;
        }

        AppTradeOrderSettlementRespVO.Address address = new AppTradeOrderSettlementRespVO.Address();

        address.setId(memberAddressRespDTO.getId());
        address.setName(memberAddressRespDTO.getName());
        address.setMobile(memberAddressRespDTO.getMobile());
        if (memberAddressRespDTO.getAreaId() != null) {
            address.setAreaId(memberAddressRespDTO.getAreaId().longValue());
        }
        address.setDetailAddress(memberAddressRespDTO.getDetailAddress());
        address.setDefaultStatus(memberAddressRespDTO.getDefaultStatus());

        return address;
    }

    default AppTradeOrderSettlementRespVO.Price priceToPrice10(TradePriceCalculateRespBO.Price price) {
        if (price == null) {
            return null;
        }

        AppTradeOrderSettlementRespVO.Price price1 = new AppTradeOrderSettlementRespVO.Price();

        price1.setTotalPrice(price.getTotalPrice());
        price1.setDiscountPrice(price.getDiscountPrice());
        price1.setDeliveryPrice(price.getDeliveryPrice());
        price1.setCouponPrice(price.getCouponPrice());
        price1.setPointPrice(price.getPointPrice());
        price1.setVipPrice(price.getVipPrice());
        price1.setPayPrice(price.getPayPrice());

        return price1;
    }

    List<AppOrderExpressTrackRespDTO> convertList02(List<ExpressTrackRespDTO> list);

    TradeOrderDO convert(TradeOrderUpdateAddressReqVO reqVO);

    TradeOrderDO convert(TradeOrderUpdatePriceReqVO reqVO);

    TradeOrderDO convert(TradeOrderRemarkReqVO reqVO);

    default BrokerageAddReqBO convert(MemberUserRespDTO user, TradeOrderItemDO item,
                                      ProductSpuRespDTO spu, ProductSkuRespDTO sku) {
        BrokerageAddReqBO bo = new BrokerageAddReqBO().setBizId(String.valueOf(item.getId())).setSourceUserId(item.getUserId())
                .setBasePrice(item.getPayPrice() * item.getCount())
                .setTitle(StrUtil.format("{}成功购买{}", user.getNickname(), item.getSpuName()))
                .setFirstFixedPrice(0).setSecondFixedPrice(0);
        if (BooleanUtil.isTrue(spu.getSubCommissionType())) {
            bo.setFirstFixedPrice(sku.getFirstBrokeragePrice()).setSecondFixedPrice(sku.getSecondBrokeragePrice());
        }
        return bo;
    }

    @Named("convertList04")
    List<TradeOrderRespDTO> convertList04(List<TradeOrderDO> list);

    @Mappings({
            @Mapping(target = "activityId", source = "order.combinationActivityId"),
            @Mapping(target = "spuId", source = "item.spuId"),
            @Mapping(target = "skuId", source = "item.skuId"),
            @Mapping(target = "count", source = "item.count"),
            @Mapping(target = "orderId", source = "order.id"),
            @Mapping(target = "userId", source = "order.userId"),
            @Mapping(target = "headId", source = "order.combinationHeadId"),
            @Mapping(target = "combinationPrice", source = "item.payPrice"),
    })
    CombinationRecordCreateReqDTO convert(TradeOrderDO order, TradeOrderItemDO item);

    default AppTradeOrderDetailRespVO convert10(TradeOrderDO order, List<TradeOrderItemDO> items) {
        if (order == null && items == null) {
            return null;
        }

        AppTradeOrderDetailRespVO appTradeOrderDetailRespVO = new AppTradeOrderDetailRespVO();

        if (order != null) {
            appTradeOrderDetailRespVO.setId(order.getId());
            appTradeOrderDetailRespVO.setNo(order.getNo());
            appTradeOrderDetailRespVO.setType(order.getType());
            appTradeOrderDetailRespVO.setCreateTime(order.getCreateTime());
            appTradeOrderDetailRespVO.setUserRemark(order.getUserRemark());
            appTradeOrderDetailRespVO.setStatus(order.getStatus());
            appTradeOrderDetailRespVO.setProductCount(order.getProductCount());
            appTradeOrderDetailRespVO.setFinishTime(order.getFinishTime());
            appTradeOrderDetailRespVO.setCancelTime(order.getCancelTime());
            appTradeOrderDetailRespVO.setCommentStatus(order.getCommentStatus());
            appTradeOrderDetailRespVO.setPayStatus(order.getPayStatus());
            appTradeOrderDetailRespVO.setPayOrderId(order.getPayOrderId());
            appTradeOrderDetailRespVO.setPayTime(order.getPayTime());
            appTradeOrderDetailRespVO.setPayChannelCode(order.getPayChannelCode());
            appTradeOrderDetailRespVO.setTotalPrice(order.getTotalPrice());
            appTradeOrderDetailRespVO.setDiscountPrice(order.getDiscountPrice());
            appTradeOrderDetailRespVO.setDeliveryPrice(order.getDeliveryPrice());
            appTradeOrderDetailRespVO.setAdjustPrice(order.getAdjustPrice());
            appTradeOrderDetailRespVO.setPayPrice(order.getPayPrice());
            appTradeOrderDetailRespVO.setDeliveryType(order.getDeliveryType());
            appTradeOrderDetailRespVO.setDeliveryTemplateId(order.getDeliveryTemplateId());
            appTradeOrderDetailRespVO.setLogisticsId(order.getLogisticsId());
            appTradeOrderDetailRespVO.setLogisticsNo(order.getLogisticsNo());
            appTradeOrderDetailRespVO.setDeliveryTime(order.getDeliveryTime());
            appTradeOrderDetailRespVO.setReceiveTime(order.getReceiveTime());
            appTradeOrderDetailRespVO.setReceiverName(order.getReceiverName());
            appTradeOrderDetailRespVO.setReceiverMobile(order.getReceiverMobile());
            appTradeOrderDetailRespVO.setReceiverAreaId(order.getReceiverAreaId());
            appTradeOrderDetailRespVO.setReceiverDetailAddress(order.getReceiverDetailAddress());
            appTradeOrderDetailRespVO.setPickUpStoreId(order.getPickUpStoreId());
            appTradeOrderDetailRespVO.setPickUpVerifyCode(order.getPickUpVerifyCode());
            appTradeOrderDetailRespVO.setCouponId(order.getCouponId());
            appTradeOrderDetailRespVO.setCouponPrice(order.getCouponPrice());
            appTradeOrderDetailRespVO.setPointPrice(order.getPointPrice());
            appTradeOrderDetailRespVO.setExchangeCode(order.getExchangeCode());

        }
        List<AppTradeOrderItemRespVO> itemRespVOList = null;
        if (items != null) {
            itemRespVOList = new ArrayList<AppTradeOrderItemRespVO>(items.size());
            for (TradeOrderItemDO bean : items) {
                AppTradeOrderItemRespVO appTradeOrderItemRespVO = new AppTradeOrderItemRespVO();
                appTradeOrderItemRespVO.setId(bean.getId());
                appTradeOrderItemRespVO.setOrderId(bean.getOrderId());
                appTradeOrderItemRespVO.setSpuId(bean.getSpuId());
                appTradeOrderItemRespVO.setSpuName(bean.getSpuName());
                appTradeOrderItemRespVO.setSkuId(bean.getSkuId());
                appTradeOrderItemRespVO.setProperties(propertyListToAppProductPropertyValueDetailRespVOList2(bean.getProperties()));
                appTradeOrderItemRespVO.setPicUrl(bean.getPicUrl());
                appTradeOrderItemRespVO.setCount(bean.getCount());
                appTradeOrderItemRespVO.setCommentStatus(bean.getCommentStatus());
                appTradeOrderItemRespVO.setPrice(bean.getPrice());
                appTradeOrderItemRespVO.setPayPrice(bean.getPayPrice());
                appTradeOrderItemRespVO.setAfterSaleId(bean.getAfterSaleId());
                appTradeOrderItemRespVO.setAfterSaleStatus(bean.getAfterSaleStatus());
                appTradeOrderItemRespVO.setIsGift(bean.getIsGift());
                itemRespVOList.add(appTradeOrderItemRespVO);
            }
        }
        appTradeOrderDetailRespVO.setItems(itemRespVOList);
        return appTradeOrderDetailRespVO;
    }

    ;

    default List<AppProductPropertyValueDetailRespVO> propertyListToAppProductPropertyValueDetailRespVOList2(List<TradeOrderItemDO.Property> list) {
        if (list == null) {
            return null;
        }

        List<AppProductPropertyValueDetailRespVO> list1 = new ArrayList<AppProductPropertyValueDetailRespVO>(list.size());
        for (TradeOrderItemDO.Property property : list) {
            list1.add(propertyToAppProductPropertyValueDetailRespVO2(property));
        }

        return list1;
    }

    default AppProductPropertyValueDetailRespVO propertyToAppProductPropertyValueDetailRespVO2(TradeOrderItemDO.Property property) {
        if (property == null) {
            return null;
        }

        AppProductPropertyValueDetailRespVO appProductPropertyValueDetailRespVO = new AppProductPropertyValueDetailRespVO();

        appProductPropertyValueDetailRespVO.setPropertyId(property.getPropertyId());
        appProductPropertyValueDetailRespVO.setPropertyName(property.getPropertyName());
        appProductPropertyValueDetailRespVO.setValueId(property.getValueId());
        appProductPropertyValueDetailRespVO.setValueName(property.getValueName());

        return appProductPropertyValueDetailRespVO;
    }

    default AppTradeOrderPageItemRespVO convert100(TradeOrderDO order, List<TradeOrderItemDO> items) {
        if (order == null && items == null) {
            return null;
        }

        AppTradeOrderPageItemRespVO appTradeOrderPageItemRespVO = new AppTradeOrderPageItemRespVO();

        if (order != null) {
            appTradeOrderPageItemRespVO.setId(order.getId());
            appTradeOrderPageItemRespVO.setNo(order.getNo());
            appTradeOrderPageItemRespVO.setType(order.getType());
            appTradeOrderPageItemRespVO.setStatus(order.getStatus());
            appTradeOrderPageItemRespVO.setProductCount(order.getProductCount());
            appTradeOrderPageItemRespVO.setCommentStatus(order.getCommentStatus());
            appTradeOrderPageItemRespVO.setCreateTime(order.getCreateTime());
            appTradeOrderPageItemRespVO.setPayOrderId(order.getPayOrderId());
            appTradeOrderPageItemRespVO.setPayPrice(order.getPayPrice());
            appTradeOrderPageItemRespVO.setDeliveryType(order.getDeliveryType());
            appTradeOrderPageItemRespVO.setPayStatus(order.getPayStatus());
            appTradeOrderPageItemRespVO.setExchangeCode(order.getExchangeCode());
        }
        List<AppTradeOrderItemRespVO> itemRespVOList = null;
        if (items != null) {
            itemRespVOList = new ArrayList<AppTradeOrderItemRespVO>(items.size());
            for (TradeOrderItemDO bean : items) {
                AppTradeOrderItemRespVO appTradeOrderItemRespVO = new AppTradeOrderItemRespVO();
                appTradeOrderItemRespVO.setId(bean.getId());
                appTradeOrderItemRespVO.setOrderId(bean.getOrderId());
                appTradeOrderItemRespVO.setSpuId(bean.getSpuId());
                appTradeOrderItemRespVO.setSpuName(bean.getSpuName());
                appTradeOrderItemRespVO.setSkuId(bean.getSkuId());
                appTradeOrderItemRespVO.setProperties(propertyListToAppProductPropertyValueDetailRespVOList2(bean.getProperties()));
                appTradeOrderItemRespVO.setPicUrl(bean.getPicUrl());
                appTradeOrderItemRespVO.setCount(bean.getCount());
                appTradeOrderItemRespVO.setCommentStatus(bean.getCommentStatus());
                appTradeOrderItemRespVO.setPrice(bean.getPrice());
                appTradeOrderItemRespVO.setPayPrice(bean.getPayPrice());
                appTradeOrderItemRespVO.setAfterSaleId(bean.getAfterSaleId());
                appTradeOrderItemRespVO.setAfterSaleStatus(bean.getAfterSaleStatus());
                appTradeOrderItemRespVO.setIsGift(bean.getIsGift());
                itemRespVOList.add(appTradeOrderItemRespVO);
            }
        }
        appTradeOrderPageItemRespVO.setItems(itemRespVOList);

        return appTradeOrderPageItemRespVO;
    }

    ;


}
