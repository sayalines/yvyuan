package cn.iocoder.yudao.server.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 抽盒详细
 */
@Data
public class ApiProductBoxDO {

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 抽盒配置ID
     */
    private Long configId;
    /**
     * 抽盒编号
     */
    private String no;
    /**
     * 抽盒名称
     */
    private String name;
    /**
     * 封面图
     */
    private String picUrl;
    /**
     * 背景图
     */
    private String backgroundUrl;
    /**
     * 开盒图
     */
    private String processUrl;
    /**
     * 未售图
     */
    private String unsoldUrl;
    /**
     * 轮播图地址数组，以逗号分隔最多上传15张
     */
    private List<String> sliderPicUrls;
    /**
     * 抽盒数量
     */
    private String qty;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    /**
     * 是否预售
     */
    private Boolean preSale;
    /**
     * 预售开始时间
     */
    private String preStartTime;
    /**
     * 抽盒详情
     */
    private String introduction;
    /**
     * 抽盒规则说明
     */
    private String description;
    /**
     * 是否优品推荐
     */
    private Boolean recommendGood;
    /**
     * 已售数量
     */
    private Integer salesCount;
    /**
     * 是否在售
     */
    private Boolean isSale;
    /**
     * 是否首单优惠商品
     */
    private Boolean isFirstProduct;
    /**
     * 产品明细
     */
    List<ApiProductBoxItemDO> items;
    /**
     * 是否设置过提醒
     */
    private Boolean isReminded;
}
