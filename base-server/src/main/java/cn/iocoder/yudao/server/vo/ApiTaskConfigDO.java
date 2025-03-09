package cn.iocoder.yudao.server.vo;

import lombok.Data;

@Data
public class ApiTaskConfigDO {
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
     * 图标
     */
    private String logo;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 奖励积分
     */
    private Integer point;
    /**
     * 奖励彩虹值
     */
    private Integer rainbow;
    /**
     * 限制类型
     */
    private Integer limitType;
    /**
     * 限制次数
     */
    private Integer limitCount;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 是否完成
     */
    private Boolean isUse;
}
