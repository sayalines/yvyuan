package cn.iocoder.yudao.server.vo;

import lombok.Data;


@Data
public class ApiMemberCardRecordDO {
    /**
     * ID
     */
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 是否使用
     */
    private Boolean isUse;
    /**
     * 有效开始时间
     */
    private String validStartTime;
    /**
     * 有效结束时间
     */
    private String validEndTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 功能卡ID
     */
    private Long cardId;
    /**
     * 功能卡名称
     */
    private String cardName;
    /**
     * 功能卡图标
     */
    private String cardLogo;
}
