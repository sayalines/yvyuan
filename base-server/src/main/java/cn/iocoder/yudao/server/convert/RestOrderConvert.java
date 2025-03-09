package cn.iocoder.yudao.server.convert;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pay.controller.admin.order.vo.PayOrderRespVO;
import cn.iocoder.yudao.module.trade.controller.app.base.property.AppProductPropertyValueDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppOrderExpressTrackRespDTO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderPageItemRespVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderSettlementRespVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.item.AppTradeOrderItemRespVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.delivery.DeliveryExpressTemplateDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderTypeEnum;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.vo.*;

import java.util.ArrayList;
import java.util.List;

public class RestOrderConvert {

    public static ApiTradeOrderSettlementDO convert(AppTradeOrderSettlementRespVO vo){
        ApiTradeOrderSettlementDO result = new ApiTradeOrderSettlementDO();
        result.setAddress(BeanUtils.toBean(vo.getAddress(),ApiTradeOrderSettlementDO.Address.class));
        result.setTotalPoint(vo.getTotalPoint());
        result.setUsedPoint(vo.getUsedPoint());
        result.setGivePoint(vo.getGivePoint());
        result.setExchangeCode(vo.getExchangeCode());
        List<ApiTradeOrderSettlementDO.Coupon> coupons = new ArrayList<>();
        if (vo.getCoupons()!=null && vo.getCoupons().size()>0){
            for(AppTradeOrderSettlementRespVO.Coupon dd:vo.getCoupons()){
                ApiTradeOrderSettlementDO.Coupon dto = BeanUtils.toBean(dd,ApiTradeOrderSettlementDO.Coupon.class);
                dto.setDiscountPrice(CommonUtils.formatPrice(dd.getDiscountPrice()));
                dto.setDiscountLimitPrice(CommonUtils.formatPrice(dd.getDiscountLimitPrice()));
                dto.setUsePrice(CommonUtils.formatPrice(dd.getUsePrice()));
                dto.setValidStartTime(CommonUtils.formatLocalDateTime(dd.getValidStartTime(),"yyyy-MM-dd HH:mm:ss"));
                dto.setValidEndTime(CommonUtils.formatLocalDateTime(dd.getValidEndTime(),"yyyy-MM-dd HH:mm:ss"));
                coupons.add(dto);
            }
        }
        result.setCoupons(coupons);
        List<ApiTradeOrderSettlementDO.Item> itemList = new ArrayList<>();
        if (vo.getItems()!=null) {
            for (AppTradeOrderSettlementRespVO.Item dd : vo.getItems()) {
                ApiTradeOrderSettlementDO.Item item = BeanUtils.toBean(dd, ApiTradeOrderSettlementDO.Item.class);
                item.setPrice(CommonUtils.formatPrice(dd.getPrice()));
                item.setSkuName(getSkuName(dd.getProperties()));
                itemList.add(item);
            }
        }
        result.setItems(itemList);
        ApiTradeOrderSettlementDO.Price price= new ApiTradeOrderSettlementDO.Price();
        if(vo.getPrice()!=null){
            price.setCouponPrice(CommonUtils.formatPrice(vo.getPrice().getCouponPrice()));
            price.setDiscountPrice(CommonUtils.formatPrice(vo.getPrice().getDiscountPrice()));
            price.setDeliveryPrice(CommonUtils.formatPrice(vo.getPrice().getDeliveryPrice()));
            price.setPayPrice(CommonUtils.formatPrice(vo.getPrice().getPayPrice()));
            price.setPointPrice(CommonUtils.formatPrice(vo.getPrice().getPointPrice()));
            price.setTotalPrice(CommonUtils.formatPrice(vo.getPrice().getTotalPrice()));
            price.setVipPrice(CommonUtils.formatPrice(vo.getPrice().getVipPrice()));
        }
        result.setPrice(price);
        return result;
    }

    public static String getSkuName(List<AppProductPropertyValueDetailRespVO> properties){
        String result ="";
        if (properties!=null && properties.size()>0){
            AppProductPropertyValueDetailRespVO property = properties.get(0);
            result = property.getValueName();
        }
        return result;
    }

    public static ApiTradeOrderDetailDO convert(AppTradeOrderDetailRespVO vo){
        ApiTradeOrderDetailDO result = new ApiTradeOrderDetailDO();
        if (vo!=null){
            result.setId(vo.getId());
            result.setNo(vo.getNo());
            result.setCreateTime(CommonUtils.formatLocalDateTime(vo.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setUserRemark(vo.getUserRemark());
            result.setStatus(vo.getStatus());
            result.setProductCount(vo.getProductCount());
            result.setFinishTime(CommonUtils.formatLocalDateTime(vo.getFinishTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setCancelTime(CommonUtils.formatLocalDateTime(vo.getCancelTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setCommentStatus(vo.getCommentStatus());
            result.setPayStatus(vo.getPayStatus());
            result.setPayOrderId(vo.getPayOrderId());
            result.setPayTime(CommonUtils.formatLocalDateTime(vo.getPayTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setPayExpireTime(CommonUtils.formatLocalDateTime(vo.getPayExpireTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setPayChannelCode(vo.getPayChannelCode());
            result.setPayChannelName(vo.getPayChannelName());
            result.setTotalPrice(CommonUtils.formatPrice(vo.getTotalPrice()));
            result.setDiscountPrice(CommonUtils.formatPrice(vo.getDiscountPrice()));
            result.setDeliveryPrice(CommonUtils.formatPrice(vo.getDeliveryPrice()));
            result.setAdjustPrice(CommonUtils.formatPrice(vo.getAdjustPrice()));
            result.setPayPrice(CommonUtils.formatPrice(vo.getPayPrice()));
            result.setDeliveryType(vo.getDeliveryType());
            result.setDeliveryTemplateId(vo.getDeliveryTemplateId());
            result.setLogisticsId(vo.getLogisticsId());
            result.setLogisticsName(vo.getLogisticsName());
            result.setLogisticsNo(vo.getLogisticsNo());
            result.setDeliveryTime(CommonUtils.formatLocalDateTime(vo.getDeliveryTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setReceiveTime(CommonUtils.formatLocalDateTime(vo.getReceiveTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setReceiverName(vo.getReceiverName());
            result.setReceiverMobile(vo.getReceiverMobile());
            result.setReceiverAreaId(vo.getReceiverAreaId());
            result.setReceiverAreaName(vo.getReceiverAreaName());
            result.setReceiverDetailAddress(vo.getReceiverDetailAddress());
            result.setPickUpStoreId(vo.getPickUpStoreId());
            result.setPickUpVerifyCode(vo.getPickUpVerifyCode());
            result.setCouponId(vo.getCouponId());
            result.setCouponPrice(CommonUtils.formatPrice(vo.getCouponPrice()));
            result.setPointPrice(CommonUtils.formatPrice(vo.getPointPrice()));
            result.setGivePoint(vo.getGivePoint());
            result.setExchangeCode(vo.getExchangeCode());
            String type="";
            if (vo.getType()!=null){
                if (vo.getType().equals(TradeOrderTypeEnum.NORMAL.getType())){
                    type = "普通订单";
                }else if (vo.getType().equals(TradeOrderTypeEnum.SECKILL.getType())){
                    type = "秒杀订单";
                }else if (vo.getType().equals(TradeOrderTypeEnum.BARGAIN.getType())){
                    type = "砍价订单";
                }else if (vo.getType().equals(TradeOrderTypeEnum.COMBINATION.getType())){
                    type = "拼团订单";
                }
            }
            result.setType(type);
            List<ApiTradeOrderItem> items = new ArrayList<>();
            if (vo.getItems()!=null){
                for(AppTradeOrderItemRespVO dd:vo.getItems()){
                    ApiTradeOrderItem item = new ApiTradeOrderItem();
                    item.setId(dd.getId());
                    item.setOrderId(dd.getOrderId());
                    item.setSpuId(dd.getSpuId());
                    item.setSpuName(dd.getSpuName());
                    item.setSkuId(dd.getSkuId());
                    item.setSkuName(getSkuName(dd.getProperties()));
                    item.setPicUrl(dd.getPicUrl());
                    item.setCount(dd.getCount());
                    item.setCommentStatus(dd.getCommentStatus());
                    item.setPrice(CommonUtils.formatPrice(dd.getPrice()));
                    item.setPayPrice(CommonUtils.formatPrice(dd.getPayPrice()));
                    item.setAfterSaleId(dd.getAfterSaleId());
                    item.setAfterSaleStatus(dd.getAfterSaleStatus());
                    items.add(item);
                }
            }
            result.setItems(items);
        }
        return result;
    }

    public static ApiTradeOrderDetailDO convert2(AppTradeOrderPageItemRespVO vo){
        ApiTradeOrderDetailDO result = new ApiTradeOrderDetailDO();
        if (vo!=null){
            result.setId(vo.getId());
            result.setNo(vo.getNo());
            result.setCreateTime(CommonUtils.formatLocalDateTime(vo.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setStatus(vo.getStatus());
            result.setProductCount(vo.getProductCount());
            result.setCommentStatus(vo.getCommentStatus());
            result.setPayOrderId(vo.getPayOrderId());
            result.setPayPrice(CommonUtils.formatPrice(vo.getPayPrice()));
            result.setDeliveryType(vo.getDeliveryType());
            result.setPayStatus(vo.getPayStatus());
            result.setExchangeCode(vo.getExchangeCode());
            String type="";
            if (vo.getType()!=null){
                if (vo.getType().equals(TradeOrderTypeEnum.NORMAL.getType())){
                    type = "普通订单";
                }else if (vo.getType().equals(TradeOrderTypeEnum.SECKILL.getType())){
                    type = "秒杀订单";
                }else if (vo.getType().equals(TradeOrderTypeEnum.BARGAIN.getType())){
                    type = "砍价订单";
                }else if (vo.getType().equals(TradeOrderTypeEnum.COMBINATION.getType())) {
                    type = "拼团订单";
                }
            }
            result.setType(type);
            List<ApiTradeOrderItem> items = new ArrayList<>();
            if (vo.getItems()!=null){
                for(AppTradeOrderItemRespVO dd:vo.getItems()){
                    ApiTradeOrderItem item = new ApiTradeOrderItem();
                    item.setId(dd.getId());
                    item.setOrderId(dd.getOrderId());
                    item.setSpuId(dd.getSpuId());
                    item.setSpuName(dd.getSpuName());
                    item.setSkuId(dd.getSkuId());
                    item.setSkuName(getSkuName(dd.getProperties()));
                    item.setPicUrl(dd.getPicUrl());
                    item.setCount(dd.getCount());
                    item.setCommentStatus(dd.getCommentStatus());
                    item.setPrice(CommonUtils.formatPrice(dd.getPrice()));
                    item.setPayPrice(CommonUtils.formatPrice(dd.getPayPrice()));
                    item.setAfterSaleId(dd.getAfterSaleId());
                    item.setAfterSaleStatus(dd.getAfterSaleStatus());
                    item.setIsGift(dd.getIsGift());
                    items.add(item);
                }
            }
            result.setItems(items);
        }
        return result;
    }

    public static PageResult<ApiTradeOrderDetailDO> convertPage(PageResult<AppTradeOrderPageItemRespVO> page){
        PageResult<ApiTradeOrderDetailDO> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        List<ApiTradeOrderDetailDO> list = new ArrayList<>();
        if (page!=null && page.getList().size()>0){
            for(AppTradeOrderPageItemRespVO dd:page.getList()){
                list.add(convert2(dd));
            }
        }
        pageResult.setList(list);
        return pageResult;
    }

    public static List<ApiOrderExpressTrackDO> convert(List<AppOrderExpressTrackRespDTO> dataList){
        List<ApiOrderExpressTrackDO> resultList = new ArrayList<>();
        if (dataList!=null && dataList.size()>0){
            for(AppOrderExpressTrackRespDTO dd:dataList){
                ApiOrderExpressTrackDO item = new ApiOrderExpressTrackDO();
                item.setContent(dd.getContent());
                item.setTime(CommonUtils.formatLocalDateTime(dd.getTime(),"yyyy-MM-dd HH:mm:ss"));
                resultList.add(item);
            }
        }
        return resultList;
    }

    public static String getSkuName2(List<TradeOrderItemDO.Property> properties){
        String result ="";
        if (properties!=null && properties.size()>0){
            TradeOrderItemDO.Property property = properties.get(0);
            result = property.getValueName();
        }
        return result;
    }


    public static ApiTradeOrderItem convert(TradeOrderItemDO data){
        ApiTradeOrderItem result= new ApiTradeOrderItem();
        if (data!=null){
            result.setId(data.getId());
            result.setOrderId(data.getOrderId());
            result.setSpuId(data.getSpuId());
            result.setSpuName(data.getSpuName());
            result.setSkuId(data.getSkuId());
            result.setSkuName(getSkuName2(data.getProperties()));
            result.setPicUrl(data.getPicUrl());
            result.setCount(data.getCount());
            result.setCommentStatus(data.getCommentStatus());
            result.setPrice(CommonUtils.formatPrice(data.getPrice()));
            result.setPayPrice(CommonUtils.formatPrice(data.getPayPrice()));
            result.setAfterSaleId(data.getAfterSaleId());
            result.setAfterSaleStatus(data.getAfterSaleStatus());
            result.setIsGift(data.getIsGift());
        }
        return result;
    }

    public static ApiPayOrderDO convert(PayOrderRespVO data){
        ApiPayOrderDO result= new ApiPayOrderDO();
        if (data!=null){
            result.setId(data.getId());
            result.setCreateTime(CommonUtils.formatLocalDateTime(data.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setAppId(data.getAppId());
            result.setChannelId(data.getChannelId());
            result.setChannelCode(data.getChannelCode());
            result.setMerchantOrderId(data.getMerchantOrderId());
            result.setSubject(data.getSubject());
            result.setBody(data.getBody());
            result.setNotifyUrl(data.getNotifyUrl());
            result.setPrice(CommonUtils.formatPrice(data.getPrice()));
            result.setChannelFeeRate(data.getChannelFeeRate());
            result.setChannelFeePrice(CommonUtils.formatPrice(data.getChannelFeePrice()));
            result.setStatus(data.getStatus());
            result.setUserIp(data.getUserIp());
            result.setExpireTime(CommonUtils.formatLocalDateTime(data.getExpireTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setSuccessTime(CommonUtils.formatLocalDateTime(data.getSuccessTime(),"yyyy-MM-dd HH:mm:ss"));
            result.setExtensionId(data.getExtensionId());
            result.setNo(data.getNo());
            result.setRefundPrice(CommonUtils.formatPrice(data.getRefundPrice()));
            result.setChannelUserId(data.getChannelUserId());
            result.setChannelOrderNo(data.getChannelOrderNo());
        }
        return result;
    }



    public static List<ApiDeliveryExpressTemplateDO> convertDeliveryExpress(List<DeliveryExpressTemplateDO> list){
        List<ApiDeliveryExpressTemplateDO> resultList = new ArrayList<>();
        if (list!=null && list.size()>0){
            for(DeliveryExpressTemplateDO dd:list){
                resultList.add(BeanUtils.toBean(dd, ApiDeliveryExpressTemplateDO.class));
            }
        }
        return resultList;
    }




}
