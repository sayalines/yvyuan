package cn.iocoder.yudao.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiProductFavoriteDO {
    /**
     * ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 商品ID
     */
    private Long spuId;
    /**
     * 商品名称
     */
    private String spuName;
    /**
     * 商品封面图
     */
    private String picUrl;
    /**
     * 创建时间（格式化）
     */
    private LocalDateTime createTime;
    /**
     * 创建时间（格式化）
     */
    private String createTimeFormat;
}
