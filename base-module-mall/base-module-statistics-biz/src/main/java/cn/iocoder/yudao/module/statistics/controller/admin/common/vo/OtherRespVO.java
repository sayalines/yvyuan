package cn.iocoder.yudao.module.statistics.controller.admin.common.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 其他统计 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OtherRespVO {

    @Schema(description = "单品上新订阅数")
    @ExcelProperty("单品上新订阅数")
    private Long productSubscribeCount;

    @Schema(description = "活动开始订阅数")
    @ExcelProperty("活动开始订阅数")
    private Long activeSubscribeCount;

    @Schema(description = "活动参与人数")
    @ExcelProperty("活动参与人数")
    private Long activeUserCount;

}
