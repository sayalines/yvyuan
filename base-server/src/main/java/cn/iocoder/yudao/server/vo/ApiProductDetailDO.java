package cn.iocoder.yudao.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品详细
 */
@Data
public class ApiProductDetailDO {

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 商品简介
     */
    private String introduction;
    /**
     * 商品详情
     */
    private String description;
    /**
     * 商品封面图
     */
    private String picUrl;
    /**
     * 商品轮播图
     */
    private List<String> sliderPicUrls;

    /**
     * 商品规格
     */
    private List<ApiProductSkuDO> skuList;
    /**
     * 商品价格
     *
     * 基于其对应的 {@link cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO#getPrice()} sku单价最低的商品的
     */
    private BigDecimal price;
    /**
     * 市场价
     *
     * 基于其对应的 {@link cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO#getMarketPrice()} sku单价最低的商品的
     */
    private BigDecimal marketPrice;
    /**
     * 库存
     *
     * 基于其对应的 {@link cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO#getStock()} 求和
     */
    private Integer stock;
    /**
     * 赠送积分
     */
    private Integer giveIntegral;
    /**
     * 商品销量
     */
    private Integer salesCount;
    /**
     * 虚拟销量
     */
    private Integer virtualSalesCount;
    /**
     * 浏览量
     */
    private Integer browseCount;
    /**
     * 赠送礼品
     *
     */
    private ApiGiftDO giveGift;
    /**
     * 是否限时购买
     *
     */
    private Boolean isLimitTime;
    /**
     * 限时开始时间
     *
     */
    private String limitTimeStart;
    /**
     * 限时结束时间
     *
     */
    private String limitTimeEnd;
    /**
     * 限购数量
     */
    private Integer limitCount;
    /**
     * 是否首单优惠商品
     */
    private Boolean isFirstProduct;

    /**
     * 是否能购买
     */
    private Boolean isCanBuy;
    /**
     * 商品参数
     */
    private String params;
}
