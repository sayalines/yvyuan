package cn.iocoder.yudao.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class ApiMemberSignInRecordDO {
    @Schema(description = "第几天签到", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer day;

    @Schema(description = "签到的分数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer point;

    @Schema(description = "签到的经验", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer experience;

    @Schema(description = "签到时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createTime;
}
