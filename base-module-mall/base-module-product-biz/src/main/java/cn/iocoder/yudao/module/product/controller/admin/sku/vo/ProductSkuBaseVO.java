package cn.iocoder.yudao.module.product.controller.admin.sku.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 商品 SKU Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ProductSkuBaseVO {

    @Schema(description = "商品 SKU 名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "清凉小短袖")
    @NotEmpty(message = "商品 SKU 名字不能为空")
    private String name;

    @Schema(description = "销售价格，单位：分", requiredMode = Schema.RequiredMode.REQUIRED, example = "1999")
    @NotNull(message = "销售价格，单位：分不能为空")
    private Integer price;

    @Schema(description = "市场价", example = "2999")
    private Integer marketPrice;

    @Schema(description = "成本价", example = "19")
    private Integer costPrice;

    @Schema(description = "条形码", example = "15156165456")
    private String barCode;

    @Schema(description = "图片地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/xx.png")
    @NotNull(message = "图片地址不能为空")
    private String picUrl;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "200")
    @NotNull(message = "库存不能为空")
    private Integer stock;

    @Schema(description = "库存",example = "200")
    private Integer allocateCount;

    @Schema(description = "预警预存", example = "10")
    private Integer warnStock;

    @Schema(description = "商品重量,单位：kg 千克", example = "1.2")
    private Double weight;

    @Schema(description = "商品体积,单位：m^3 平米", example = "2.5")
    private Double volume;

    @Schema(description = "一级分销的佣金，单位：分", example = "199")
    private Integer firstBrokeragePrice;

    @Schema(description = "二级分销的佣金，单位：分", example = "19")
    private Integer secondBrokeragePrice;

    @Schema(description = "属性数组")
    private List<Property> properties;

    @Schema(description = "商品属性")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Property {

        @Schema(description = "属性编号", example = "10")
        private Long propertyId;

        @Schema(description = "属性名字", example = "颜色")
        private String propertyName;

        @Schema(description = "属性值编号", example = "10")
        private Long valueId;

        @Schema(description = "属性值名字", example = "红色")
        private String valueName;

    }

}
