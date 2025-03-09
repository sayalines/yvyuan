package cn.iocoder.yudao.module.member.controller.admin.messageremind.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 会员消息提醒 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MessageRemindRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15521")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "8470")
    @ExcelProperty("用户编号")
    private Long userId;

    @Schema(description = "openID", example = "30870")
    @ExcelProperty("openID")
    private String openid;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("业务类型")
    private Integer bizType;

    @Schema(description = "业务ID", example = "4621")
    @ExcelProperty("业务ID")
    private Long bizId;

    @Schema(description = "是否已提醒", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否已提醒")
    private Boolean isRemind;

    @Schema(description = "提醒时间")
    @ExcelProperty("提醒时间")
    private LocalDateTime remindTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}