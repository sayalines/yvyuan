package cn.iocoder.yudao.server.vo;

import lombok.Data;

@Data
public class ApiDeliveryExpressTemplateDO {
    /**
     * ID
     */
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 图标
     */
    private String logo;
}
