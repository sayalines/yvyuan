package cn.iocoder.yudao.server.vo;

import cn.iocoder.yudao.module.trade.controller.app.base.property.AppProductPropertyValueDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.app.base.sku.AppProductSkuBaseRespVO;
import cn.iocoder.yudao.module.trade.controller.app.base.spu.AppProductSpuBaseRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class ApiCartSkuDO {
    /**
     * SKU ID
     */
    private Long id;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 销售价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private Integer stock;
    /**
     * 规格名称
     */
    private String skuName;

    /**
     * 属性数组
     */
    private List<AppProductPropertyValueDetailRespVO> properties;
}
