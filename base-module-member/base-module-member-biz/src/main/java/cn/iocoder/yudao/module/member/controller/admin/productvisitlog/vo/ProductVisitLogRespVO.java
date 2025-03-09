package cn.iocoder.yudao.module.member.controller.admin.productvisitlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 商品访问日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductVisitLogRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32152")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "统计时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("统计时间")
    private LocalDateTime statTime;

    @Schema(description = "用户ID", example = "14706")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "商品ID", example = "29704")
    @ExcelProperty("商品ID")
    private Long spuId;

    @Schema(description = "商品名称", example = "李四")
    @ExcelProperty("商品名称")
    private String spuName;

    @Schema(description = "点击量", example = "6678")
    @ExcelProperty("点击量")
    private Integer hitCount;

    @Schema(description = "加购件数", example = "16653")
    @ExcelProperty("加购件数")
    private Integer cartCount;

    @Schema(description = "第一次访问时间")
    @ExcelProperty("第一次访问时间")
    private LocalDateTime minTime;

    @Schema(description = "最后一次访问时间")
    @ExcelProperty("最后一次访问时间")
    private LocalDateTime maxTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}