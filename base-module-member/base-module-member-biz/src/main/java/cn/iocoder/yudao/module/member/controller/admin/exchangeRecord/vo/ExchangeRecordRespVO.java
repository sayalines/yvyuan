package cn.iocoder.yudao.module.member.controller.admin.exchangeRecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 会员兑换记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ExchangeRecordRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23005")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13336")
    @ExcelProperty("用户编号")
    private Long userId;

    @Schema(description = "昵称", example = "张三")
    private String nickname;

    @Schema(description = "配置ID", example = "17866")
    @ExcelProperty("配置ID")
    private Long configId;

    @Schema(description = "配置明细ID", example = "17866")
    @ExcelProperty("配置明细ID")
    private Long configItemId;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("业务类型")
    private Integer bizType;

    @Schema(description = "物品ID", example = "2")
    private Long bizId;

    @Schema(description = "是否使用", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否使用")
    private Boolean isUse;

    @Schema(description = "使用时间")
    @ExcelProperty("使用时间")
    private LocalDateTime useTime;

    @Schema(description = "激活时间")
    @ExcelProperty("激活时间")
    private LocalDateTime activeTime;

    @Schema(description = "有效开始时间")
    @ExcelProperty("有效开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "有效结束时间")
    @ExcelProperty("有效结束时间")
    private LocalDateTime validEndTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "结果ID")
    @ExcelProperty("结果ID")
    private Long resultId;

}