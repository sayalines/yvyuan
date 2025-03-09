package cn.iocoder.yudao.server.vo;

import cn.iocoder.yudao.module.trade.controller.app.base.spu.AppProductSpuBaseRespVO;
import lombok.Data;

@Data
public class ApiCartDO {
    /**
     * 购物项ID
     */
    private Long id;

    /**
     * 商品数量
     */
    private Integer count;

    /**
     * 是否选中
     */
    private Boolean selected;

    /**
     * 商品 SPU
     */
    private AppProductSpuBaseRespVO spu;
    /**
     * 商品 SKU
     */
    private ApiCartSkuDO sku;
}
