package cn.iocoder.yudao.server.convert;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.product.controller.admin.spu.vo.ProductUnionVo;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.vo.*;

import java.util.ArrayList;
import java.util.List;


public class RestProductConvert {

    public static PageResult<IndexProductDO> convertProductUnionPage(PageResult<ProductUnionVo> page){
        PageResult<IndexProductDO> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        List<IndexProductDO> list = new ArrayList<>();
        if (page!=null && page.getList().size()>0){
            for(ProductUnionVo dd:page.getList()){
                list.add(convert(dd));
            }
        }
        pageResult.setList(list);
        return pageResult;
    }

    public static IndexProductDO convert(ProductUnionVo product){
        IndexProductDO dto = new IndexProductDO();
        dto.setId(product.getId());
        dto.setType(product.getType());
        dto.setName(product.getName());
        dto.setPicUrl(product.getPicUrl());
        dto.setPrice(CommonUtils.formatPrice(product.getPrice()));
        dto.setPreSale(product.getPreSale());
        if (product.getMarketPrice()!=null){
            dto.setMarketPrice(CommonUtils.formatPrice(product.getMarketPrice()));
        }
        dto.setIsFirstProduct(product.getIsFirstProduct());
        return dto;
    }

    public static PageResult<IndexProductDO> convertProductSpuPage(PageResult<ProductSpuDO> page){
        PageResult<IndexProductDO> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        List<IndexProductDO> list = new ArrayList<>();
        if (page!=null && page.getList().size()>0){
            for(ProductSpuDO dd:page.getList()){
                list.add(convert(dd));
            }
        }
        pageResult.setList(list);
        return pageResult;
    }



    public static IndexProductDO convert(ProductSpuDO product){
        IndexProductDO dto = new IndexProductDO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPicUrl(product.getPicUrl());
        dto.setPrice(CommonUtils.formatPrice(product.getPrice()));
        dto.setPreSale(false);
        if (product.getMarketPrice()!=null){
            dto.setMarketPrice(CommonUtils.formatPrice(product.getMarketPrice()));
        }
        dto.setIsFirstProduct(product.getIsFirstProduct());
        dto.setStock(product.getStock());
        return dto;
    }

    public static ApiProductDetailDO convert(ProductSpuDO product, List<ProductSkuDO> skus){
        ApiProductDetailDO dto = new ApiProductDetailDO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setKeyword(product.getKeyword());
        dto.setIntroduction(product.getIntroduction());
        dto.setDescription(product.getDescription());
        dto.setPicUrl(product.getPicUrl());
        dto.setSliderPicUrls(product.getSliderPicUrls());
        dto.setPrice(CommonUtils.formatPrice(product.getPrice()));
        if (product.getMarketPrice()!=null){
            dto.setMarketPrice(CommonUtils.formatPrice(product.getMarketPrice()));
        }
        dto.setStock(product.getStock());
        dto.setGiveIntegral(product.getGiveIntegral());
        dto.setSalesCount(product.getSalesCount());
        dto.setVirtualSalesCount(product.getVirtualSalesCount());
        dto.setBrowseCount(product.getBrowseCount());
        dto.setIsLimitTime(product.getIsLimitTime());
        dto.setLimitTimeStart(CommonUtils.formatLocalDateTime(product.getLimitTimeStart(),"yyyy-MM-dd HH:mm:ss"));
        dto.setLimitTimeEnd(CommonUtils.formatLocalDateTime(product.getLimitTimeEnd(),"yyyy-MM-dd HH:mm:ss"));
        dto.setLimitCount(product.getLimitCount());
        dto.setIsFirstProduct(product.getIsFirstProduct());
        dto.setParams(product.getParams());
        List<ApiProductSkuDO> skuList = new ArrayList<>();
        if (skus!=null && skus.size()>0){
            for(ProductSkuDO dd:skus){
                //判断库存数是否满足，不满足就不显示这个规格
                Integer stock = CommonUtils.formatInteger(dd.getStock());
                Integer allocateCount =  CommonUtils.formatInteger(dd.getAllocateCount());
                Integer allowQty =stock-allocateCount;
                if (allowQty.compareTo(0)>0){
                    ApiProductSkuDO sku = new ApiProductSkuDO();
                    sku.setId(dd.getId());
                    sku.setProperties(dd.getProperties());
                    sku.setSkuName(getSkuName(dd.getProperties()));
                    sku.setPrice(CommonUtils.formatPrice(dd.getPrice()));
                    sku.setMarketPrice(CommonUtils.formatPrice(dd.getMarketPrice()));
                    sku.setPicUrl(dd.getPicUrl());
                    sku.setStock(allowQty);
                    sku.setWeight(dd.getWeight());
                    sku.setVolume(dd.getVolume());
                    sku.setSalesCount(dd.getSalesCount());
                    skuList.add(sku);
                }
            }
        }
        dto.setSkuList(skuList);
        return dto;
    }

    public static String getSkuName(List<ProductSkuDO.Property> properties){
        String result ="";
        if (properties!=null && properties.size()>0){
            ProductSkuDO.Property property = properties.get(0);
            result = property.getValueName();
        }
        return result;
    }
}
