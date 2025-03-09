package cn.iocoder.yudao.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ApiQuestionFactorOrDimensionDO {
    /**
     * ID
     */
    private Long id;
    /**
     * 主题
     */
    private String name;
    /**
     * 简介
     */
    private String description;
    /**
     * 推荐
     */
    private String suggest;
    /**
     * 分值计算类型
     */
    private Integer scoreType;
    /**
     * 常量值
     */
    private BigDecimal constantValue;
    /**
     * 常模值
     */
    private BigDecimal constantScore;
    /**
     * 常模类型
     */
    private Integer constantScoreType;
    /**
     * 是否为总分汇总
     */
    private Integer isTotal;

    /**
     * 是否需要显示
     */
    private Integer isShow;
    /**
     * 分值
     */
    private BigDecimal score;
    /**
     * 分值整数
     */
    private Integer scoreInt;
    /**
     * 分值详细
     */
    private ApiScoreDO scoreDetail;
    /**
     * 排序号
     */
    private Integer orderNo;
    /**
     * 自定义排序号
     */
    private String customOrder;
    /**
     * 关键字内容
     */
    private List<ApiKeywordContentDO> keywordList;
}
