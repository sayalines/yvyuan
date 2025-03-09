package cn.iocoder.yudao.module.product.api.sku;

import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuUpdateStockReqDTO;
import cn.iocoder.yudao.module.product.convert.sku.ProductSkuConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.product.service.sku.ProductSkuService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 商品 SKU API 实现类
 *
 * @author LeeYan9
 * @since 2022-09-06
 */
@Service
@Validated
public class ProductSkuApiImpl implements ProductSkuApi {

    @Resource
    private ProductSkuService productSkuService;

    @Override
    public ProductSkuRespDTO getSku(Long id) {
        ProductSkuDO sku = productSkuService.getSku(id);
        return ProductSkuConvert.INSTANCE.convert02(sku);
    }

    @Override
    public List<ProductSkuRespDTO> getSkuList(Collection<Long> ids) {
        List<ProductSkuDO> skus = productSkuService.getSkuList(ids);
        return ProductSkuConvert.INSTANCE.convertList04(skus);
    }

    @Override
    public List<ProductSkuRespDTO> getSkuListBySpuId(Collection<Long> spuIds) {
        List<ProductSkuDO> skus = productSkuService.getSkuListBySpuId(spuIds);
        return ProductSkuConvert.INSTANCE.convertList04(skus);
    }

    @Override
    public void updateSkuStock(ProductSkuUpdateStockReqDTO updateStockReqDTO) {
        productSkuService.updateSkuStock(updateStockReqDTO);
    }

    @Override
    public ProductSkuRespDTO getDefaultSku(Long spuId) {
        ProductSkuRespDTO result = null;
        if (spuId!=null){
            ProductSkuDO sku = null;
            List<ProductSkuDO> skuList = productSkuService.getSkuListBySpuId(spuId);
            if (skuList!=null && skuList.size()>0){
                sku = skuList.get(0);
            }
            if (sku!=null){
                result= ProductSkuConvert.INSTANCE.convert02(sku);
            }
        }
        return result;
    }

    public void updateSkuAllocateCount(Long skuId,Integer count){
        ProductSkuDO entity = productSkuService.getSku(skuId);
        if (entity!=null){
            Integer allocateCount = entity.getAllocateCount();
            if (allocateCount==null){
                allocateCount = 0;
            }
            allocateCount = allocateCount-count;
            if (allocateCount<0){
                allocateCount = 0;
            }
            entity.setAllocateCount(allocateCount);
            productSkuService.updateSku(entity);
        }
    }

}
