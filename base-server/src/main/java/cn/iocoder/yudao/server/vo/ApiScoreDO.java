package cn.iocoder.yudao.server.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApiScoreDO {
    /**
     * 名称
     */
    private String name;
    /**
     * 起始值
     */
    private BigDecimal scoreBegin;
    /**
     * 起始值类型
     */
    private Integer scoreBeginType;
    /**
     * 终止值
     */
    private BigDecimal scoreEnd;
    /**
     * 终止值类型
     */
    private Integer scoreEndType;
    /**
     * 简介
     */
    private String description;
    /**
     * 推荐
     */
    private String suggest;
}
