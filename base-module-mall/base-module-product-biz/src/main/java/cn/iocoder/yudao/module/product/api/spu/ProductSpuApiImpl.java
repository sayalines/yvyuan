package cn.iocoder.yudao.module.product.api.spu;

import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.yudao.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.yudao.module.product.convert.spu.ProductSpuConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.yudao.module.product.service.spu.ProductSpuService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 商品 SPU API 接口实现类
 *
 * @author LeeYan9
 * @since 2022-09-06
 */
@Service
@Validated
public class ProductSpuApiImpl implements ProductSpuApi {

    @Resource
    private ProductSpuService spuService;

    @Override
    public List<ProductSpuRespDTO> getSpuList(Collection<Long> ids) {
        List<ProductSpuDO> list = spuService.getSpuList(ids);
        List<ProductSpuRespDTO> resList = new ArrayList<>();
        if (list!=null && list.size()>0){
            for(ProductSpuDO spu:list){
                ProductSpuRespDTO respDTO = ProductSpuConvert.INSTANCE.convert02(spu);
                if (respDTO!=null){
                    respDTO.setIsLimitTime(spu.getIsLimitTime());
                    respDTO.setLimitTimeStart(spu.getLimitTimeStart());
                    respDTO.setLimitTimeEnd(spu.getLimitTimeEnd());
                    respDTO.setLimitCount(spu.getLimitCount());
                    respDTO.setGiveGift(spu.getGiveGift());
                }
                resList.add(respDTO);
            }
        }
        return resList;
    }

    @Override
    public List<ProductSpuRespDTO> validateSpuList(Collection<Long> ids) {
        return ProductSpuConvert.INSTANCE.convertList2(spuService.validateSpuList(ids));
    }

    @Override
    public ProductSpuRespDTO getSpu(Long id) {
        ProductSpuDO spu = spuService.getSpu(id);
        ProductSpuRespDTO respDTO = ProductSpuConvert.INSTANCE.convert02(spu);
        if (respDTO!=null){
            respDTO.setIsLimitTime(spu.getIsLimitTime());
            respDTO.setLimitTimeStart(spu.getLimitTimeStart());
            respDTO.setLimitTimeEnd(spu.getLimitTimeEnd());
            respDTO.setLimitCount(spu.getLimitCount());
            respDTO.setGiveGift(spu.getGiveGift());
        }
        return respDTO;
    }

}
