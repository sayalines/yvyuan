package cn.iocoder.yudao.server.convert;


import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.trade.controller.app.base.property.AppProductPropertyValueDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.app.base.sku.AppProductSkuBaseRespVO;
import cn.iocoder.yudao.module.trade.controller.app.cart.vo.AppCartListRespVO;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.vo.ApiCartDO;
import cn.iocoder.yudao.server.vo.ApiCartSkuDO;

import java.util.ArrayList;
import java.util.List;


public class RestCartConvert {

    public static List<ApiCartDO> convertList(AppCartListRespVO respVO){
        List<ApiCartDO> resultList = new ArrayList<>();
        if (respVO!=null){
            for(AppCartListRespVO.Cart dd:respVO.getValidList()){
                resultList.add(convert(dd));
            }
        }
        return resultList;
    }

    public static ApiCartDO convert(AppCartListRespVO.Cart cart){
        ApiCartDO result = new ApiCartDO();
        if (cart!=null){
            result.setId(cart.getId());
            result.setCount(cart.getCount());
            result.setSelected(cart.getSelected());
            result.setSpu(cart.getSpu());
            result.setSku(convertSku(cart.getSku()));
        }
        return result;
    }

    public static ApiCartSkuDO convertSku(AppProductSkuBaseRespVO sku){
        ApiCartSkuDO result = new ApiCartSkuDO();
        if (sku!=null){
            result.setId(sku.getId());
            result.setPicUrl(sku.getPicUrl());
            result.setPrice(CommonUtils.formatPrice(sku.getPrice()));
            result.setProperties(sku.getProperties());
            result.setSkuName(getSkuName(sku.getProperties()));
        }
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
}
