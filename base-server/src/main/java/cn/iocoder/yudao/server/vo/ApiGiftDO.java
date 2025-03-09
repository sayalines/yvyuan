package cn.iocoder.yudao.server.vo;

import lombok.Data;


/**
 * 赠送礼品
 */
@Data
public class ApiGiftDO {

    /**
     * ID
     */
    private String id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 图片地址
     */
    private String picUrl;


}
