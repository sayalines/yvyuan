package cn.iocoder.yudao.module.product.convert.sku;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuUpdateStockReqDTO;
import cn.iocoder.yudao.module.product.controller.admin.sku.vo.ProductSkuCreateOrUpdateReqVO;
import cn.iocoder.yudao.module.product.controller.admin.sku.vo.ProductSkuRespVO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 商品 SKU Convert
 *
 * @author 商城管理系统
 */
@Mapper
public interface ProductSkuConvert {

    ProductSkuConvert INSTANCE = Mappers.getMapper(ProductSkuConvert.class);

    ProductSkuDO convert(ProductSkuCreateOrUpdateReqVO bean);

    ProductSkuRespVO convert(ProductSkuDO bean);

    List<ProductSkuRespVO> convertList(List<ProductSkuDO> list);

    List<ProductSkuDO> convertList06(List<ProductSkuCreateOrUpdateReqVO> list);

    default List<ProductSkuDO> convertList06(List<ProductSkuCreateOrUpdateReqVO> list, Long spuId) {
        List<ProductSkuDO> result = convertList06(list);
        result.forEach(item -> item.setSpuId(spuId));
        return result;
    }

    ProductSkuRespDTO convert02(ProductSkuDO bean);

    List<ProductSkuRespDTO> convertList04(List<ProductSkuDO> list);

    /**
     * 获得 SPU 的库存变化 Map
     *
     * @param items SKU 库存变化
     * @param skus SKU 列表
     * @return SPU 的库存变化 Map
     */
    default Map<Long, Integer> convertSpuStockMap(List<ProductSkuUpdateStockReqDTO.Item> items,
                                                  List<ProductSkuDO> skus) {
        Map<Long, Long> skuIdAndSpuIdMap = convertMap(skus, ProductSkuDO::getId, ProductSkuDO::getSpuId); // SKU 与 SKU 编号的 Map 关系
        Map<Long, Integer> spuIdAndStockMap = new HashMap<>(); // SPU 的库存变化 Map 关系
        items.forEach(item -> {
            Long spuId = skuIdAndSpuIdMap.get(item.getId());
            if (spuId == null) {
                return;
            }
            Integer stock = spuIdAndStockMap.getOrDefault(spuId, 0) + item.getIncrCount();
            spuIdAndStockMap.put(spuId, stock);
        });
        return spuIdAndStockMap;
    }

    default String buildPropertyKey(ProductSkuDO bean) {
        if (CollUtil.isEmpty(bean.getProperties())) {
            return StrUtil.EMPTY;
        }
        List<ProductSkuDO.Property> properties = new ArrayList<>(bean.getProperties());
        properties.sort(Comparator.comparing(ProductSkuDO.Property::getValueId));
        return properties.stream().map(m -> String.valueOf(m.getValueId())).collect(Collectors.joining());
    }

}
