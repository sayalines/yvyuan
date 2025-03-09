package cn.iocoder.yudao.server.vo;

import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品明细
 */
@Data
public class ApiProductBoxItemDO {

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 序次
     */
    private Integer no;
    /**
     * 是否已售
     */
    private Boolean isSale;
    /**
     * 是否使用功能卡
     */
    private Boolean isUseCard;

}
