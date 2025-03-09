package cn.iocoder.yudao.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class ApiPointDO {

    @Schema(description = "自增主键")
    private Long id;;

    @Schema(description = "积分标题")
    private String title;

    @Schema(description = "积分描述")
    private String description;

    @Schema(description = "积分")
    private Integer point;

    @Schema(description = "发生时间")
    private String createTime;

    @Schema(description = "业务类型")
    private Integer bizType;

    @Schema(description = "业务类型描述")
    private String bizTypeDesc;

    @Schema(description = "变动后的积分")
    private Integer totalPoint;
}
