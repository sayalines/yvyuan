package cn.iocoder.yudao.module.product.controller.admin.spu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 商品规格 Response VO")
@Data
@ToString(callSuper = true)
public class ProductSkuVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

}
