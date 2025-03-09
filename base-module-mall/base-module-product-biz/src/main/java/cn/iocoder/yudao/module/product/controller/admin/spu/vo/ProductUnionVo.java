package cn.iocoder.yudao.module.product.controller.admin.spu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品联合查询结果
 */
@Data
public class ProductUnionVo {
    /**
     * 自增主键
     */
    private Long id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
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
    private Integer price;
    /**
     * 是否预售
     */
    private Boolean preSale;

    /**
     * 是否推荐
     */
    private Boolean recommend;

    /**
     * 市场价
     */
    private Integer marketPrice;

    /**
     * 是否首单优惠商品
     */
    private Boolean isFirstProduct;

}
