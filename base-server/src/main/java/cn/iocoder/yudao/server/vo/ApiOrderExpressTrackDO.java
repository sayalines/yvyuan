package cn.iocoder.yudao.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApiOrderExpressTrackDO {
    @Schema(description = "发生时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String time;

    @Schema(description = "快递状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "已签收")
    private String content;
}
