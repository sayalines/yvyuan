package cn.iocoder.yudao.server.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class ApiRainbowDO {

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 业务编码
     */
    private String bizId;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 彩虹值
     */
    private Integer rainbow;
    /**
     * 变动后的彩虹值
     */
    private Integer totalRainbow;

    @Schema(description = "发生时间")
    private String createTime;

    @Schema(description = "业务类型描述")
    private String bizTypeDesc;
}
