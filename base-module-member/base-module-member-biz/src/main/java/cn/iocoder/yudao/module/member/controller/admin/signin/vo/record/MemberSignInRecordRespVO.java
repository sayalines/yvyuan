package cn.iocoder.yudao.module.member.controller.admin.signin.vo.record;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 签到记录 Response VO")
@Data
public class MemberSignInRecordRespVO {

    @Schema(description = "签到自增 id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11903")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "签到用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "6507")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "昵称", example = "张三")
    @ExcelProperty("用户昵称")
    private String nickname;

    @Schema(description = "第几天签到", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("第几天签到")
    private Integer day;

    @Schema(description = "签到获取积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @ExcelProperty("签到获取积分")
    private Integer point;

    @Schema(description = "签到时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("签到时间")
    private LocalDateTime createTime;

}
