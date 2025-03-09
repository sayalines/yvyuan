package cn.iocoder.yudao.module.statistics.controller.admin.member.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 访客行为 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MemberActionExcelVO {
    @Schema(description = "访问页面")
    @ExcelProperty("访问页面")
    private String title;

    @Schema(description = "PV")
    @ExcelProperty("PV")
    private Integer pv;

    @Schema(description = "UV")
    @ExcelProperty("UV")
    private Integer uv;

    @Schema(description = "点击数")
    @ExcelProperty("点击数")
    private Integer clickCount;

    @Schema(description = "页面曝光点击率")
    @ExcelProperty("页面曝光点击率")
    private String exposureRate;

    @Schema(description = "新访客数")
    @ExcelProperty("新访客数")
    private Integer newUserCount;

    @Schema(description = "老访客数")
    @ExcelProperty("老访客数")
    private Integer oldUserCount;
}
