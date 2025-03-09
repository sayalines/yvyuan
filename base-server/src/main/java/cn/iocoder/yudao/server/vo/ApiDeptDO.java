package cn.iocoder.yudao.server.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApiDeptDO {

    /**
     * 部门编号
     */
    @TableId
    private Long id;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String name;
}
