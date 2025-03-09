package cn.iocoder.yudao.module.product.controller.admin.spu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 商品树 Response VO")
@Data
@ToString(callSuper = true)
public class ProductTreeVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "父分类ID")
    private Long parentId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "ID")
    private Boolean leaf;

}
