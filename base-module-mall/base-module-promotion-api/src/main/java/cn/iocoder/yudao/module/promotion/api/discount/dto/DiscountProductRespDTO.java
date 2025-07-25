package cn.iocoder.yudao.module.promotion.api.discount.dto;

import lombok.Data;

/**
 * 限时折扣活动商品 Response DTO
 *
 * @author 商城管理系统
 */
@Data
public class DiscountProductRespDTO {

    /**
     * 编号，主键自增
     */
    private Long id;
    /**
     * 商品 SPU 编号
     */
    private Long spuId;
    /**
     * 商品 SKU 编号
     */
    private Long skuId;
    /**
     * 折扣类型
     */
    private Integer discountType;
    /**
     * 折扣百分比
     */
    private Integer discountPercent;
    /**
     * 优惠金额，单位：分
     */
    private Integer discountPrice;

    // ========== 活动字段 ==========
    /**
     * 限时折扣活动的编号
     */
    private Long activityId;
    /**
     * 活动标题
     */
    private String activityName;

}
