package cn.iocoder.yudao.module.promotion.api.coupon.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 优惠劵使用 Request DTO
 *
 * @author 商城管理系统
 */
@Data
public class CouponValidReqDTO {

    /**
     * 优惠劵编号
     */
    @NotNull(message = "优惠劵编号不能为空")
    private Long id;

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private Long userId;

}
