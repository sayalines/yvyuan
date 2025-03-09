package cn.iocoder.yudao.server.vo;

import lombok.Data;

@Data
public class ApiExchangeRecordDO {
    /**
     * 自增主键
     */
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 配置ID
     */
    private Long configId;
    /**
     * 配置明细ID
     */
    private Long configItemId;
    /**
     * 编码
     */
    private String code;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 物品ID
     */
    private Long bizId;
    /**
     * 是否使用
     */
    private Boolean isUse;
    /**
     * 使用时间
     */
    private String useTime;
    /**
     * 激活时间
     */
    private String activeTime;
    /**
     * 有效开始时间
     */
    private String validStartTime;
    /**
     * 有效结束时间
     */
    private String validEndTime;

    /**
     * 结果ID
     */
    private Long resultId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 商品
     */
    private ApiProductDetailDO product;
    /**
     * 优惠劵
     */
    private ApiCouponDO coupon;
    /**
     * 功能卡
     */
    private ApiMemberCardRecordDO cardRecord;
}
