package cn.iocoder.yudao.module.member.controller.admin.productvisitlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 商品访问日志新增/修改 Request VO")
@Data
public class ProductVisitLogSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32152")
    private Long id;

    @Schema(description = "统计时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "统计时间不能为空")
    private LocalDateTime statTime;

    @Schema(description = "用户ID", example = "14706")
    private Long userId;

    @Schema(description = "商品ID", example = "29704")
    private Long spuId;

    @Schema(description = "商品名称", example = "李四")
    private String spuName;

    @Schema(description = "点击量", example = "6678")
    private Integer hitCount;

    @Schema(description = "加购件数", example = "16653")
    private Integer cartCount;

    @Schema(description = "第一次访问时间")
    private LocalDateTime minTime;

    @Schema(description = "最后一次访问时间")
    private LocalDateTime maxTime;

}