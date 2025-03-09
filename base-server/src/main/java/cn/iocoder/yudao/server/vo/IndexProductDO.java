package cn.iocoder.yudao.server.vo;

import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页推荐产品
 */
@Data
public class IndexProductDO {

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 封面图
     */
    private String picUrl;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 是否预售
     */
    private Boolean preSale;

    /**
     * 单位
     */
    private String unit;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;

    /**
     * 是否首单优惠商品
     */
    private Boolean isFirstProduct;
    /**
     * 库存
     *
     */
    private Integer stock;

}
