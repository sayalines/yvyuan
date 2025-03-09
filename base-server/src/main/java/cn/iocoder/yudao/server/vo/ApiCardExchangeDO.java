package cn.iocoder.yudao.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiCardExchangeDO {
    /**
     * 自增主键
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 积分
     */
    private Integer point;
    /**
     * 彩虹值
     */
    private Integer rainbow;
    /**
     * 限制数量
     */
    private Integer limitCount;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 生效日期类型
     */
    private Integer validityType;
    /**
     * 有效开始时间
     */
    private LocalDateTime validStartTime;
    /**
     * 有效结束时间
     */
    private LocalDateTime validEndTime;
    /**
     * 领取开始天数
     */
    private Integer fixedStartTerm;
    /**
     * 领取结束天数
     */
    private Integer fixedEndTerm;
    /**
     * 优惠劵模板ID
     */
    private Long couponTemplateId;
    /**
     * 图标
     */
    private String logo;
}
