package cn.iocoder.yudao.module.member.api.config.dto;

import lombok.Data;

/**
 * 任务配置 Response DTO
 *
 * @author 商城管理系统
 */
@Data
public class TaskConfigRespDTO {

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

}
