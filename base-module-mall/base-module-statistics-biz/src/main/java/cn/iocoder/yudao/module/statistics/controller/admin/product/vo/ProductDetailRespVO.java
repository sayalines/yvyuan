package cn.iocoder.yudao.module.statistics.controller.admin.product.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 商品明细统计 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductDetailRespVO {
    @Schema(description = "商品ID")
    @ExcelProperty("商品ID")
    private String spuId;

    @Schema(description = "商品名称")
    @ExcelProperty("商品名称")
    private String spuName;

    @Schema(description = "点击量")
    @ExcelProperty("点击量")
    private Integer hitCount;

    @Schema(description = "访客量")
    @ExcelProperty("访客量")
    private Integer visitCount;

    @Schema(description = "商品点击率")
    @ExcelProperty("商品点击率")
    private String hitRate;

    @Schema(description = "加购人数")
    @ExcelProperty("加购人数")
    private Integer cartUserCount;

    @Schema(description = "加购件数")
    @ExcelProperty("加购件数")
    private Integer cartCount;

    @Schema(description = "加购率")
    @ExcelProperty("加购率")
    private String cartRate;

    @Schema(description = "下单人数")
    @ExcelProperty("下单人数")
    private Integer orderUserCount;

    @Schema(description = "成交人数")
    @ExcelProperty("成交人数")
    private Integer payUserCount;

    @Schema(description = "成交金额")
    @ExcelProperty("成交金额")
    private BigDecimal payAmount;

    @Schema(description = "成交单数")
    @ExcelProperty("成交单数")
    private Integer payOrderCount;

    @Schema(description = "平均发货时长")
    @ExcelProperty("平均发货时长")
    private Integer deliveryTime;
}
